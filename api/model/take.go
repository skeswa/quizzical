package model

import (
	"database/sql"
	"time"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableTake           = "gauntlet.quiz_take"
	columnTakeID        = "id"
	columnTakeQuizID    = "quiz_id"
	columnTakeDateTaken = "date_taken"

	tableTakeAnswer                = "gauntlet.quiz_take_answer"
	columnTakeAnswerID             = "id"
	columnTakeAnswerAnswer         = "answer"
	columnTakeAnswerTakeID         = "quiz_take_id"
	columnTakeAnswerCorrect        = "correct"
	columnTakeAnswerQuestionID     = "question_id"
	columnTakeAnswerTimesSkipped   = "times_skipped"
	columnTakeAnswerSecondsElapsed = "seconds_elapsed"
)

// Take is the model used for takes.
type Take struct {
	ID        int
	Quiz      *Quiz
	Answers   []*TakeAnswer
	DateTaken time.Time
}

// TakeAnswer is the model used for take answers.
type TakeAnswer struct {
	ID             int
	Answer         string
	Correct        bool
	Question       *Question
	TimesSkipped   int
	SecondsElapsed int
}

// ToDTO turns this model into a DTO.
func (t *Take) ToDTO() *dto.Take {
	var answers []dto.TakeAnswer

	for _, answer := range t.Answers {
		var question *dto.Question
		if answer.Question != nil {
			question = answer.Question.ToDTO()
		}

		answers = append(answers, dto.TakeAnswer{
			ID:             &answer.ID,
			Answer:         answer.Answer,
			Correct:        answer.Correct,
			Question:       question,
			TimesSkipped:   answer.TimesSkipped,
			SecondsElapsed: answer.SecondsElapsed,
		})
	}

	var quiz *dto.Quiz
	if t.Quiz != nil {
		quiz = t.Quiz.ToDTO()
	}

	return &dto.Take{
		ID:        &t.ID,
		Quiz:      quiz,
		Answers:   answers,
		DateTaken: &t.DateTaken,
	}
}

// Takes is a collection of take models.
type Takes []*Take

// ToDTO turns these models into DTOs.
func (d Takes) ToDTO() []*dto.Take {
	dtos := []*dto.Take{}

	// Turn each model into a DTO.
	for _, take := range d {
		dtos = append(dtos, take.ToDTO())
	}

	return dtos
}

// InsertTake inserts a take into the db.
func InsertTake(db *sql.DB, quizID int, answers []dto.TakeAnswer) (int, error) {
	var (
		id  int
		err error
	)

	// Start the insertion transaction.
	tx, err := db.Begin()
	if err != nil {
		return -1, err
	}

	// Create the take first and foremost.
	if err = sq.Insert(tableTake).
		Columns(columnTakeQuizID, columnTakeDateTaken).
		Values(quizID, sqlNow).
		Suffix(returningIDClause).
		PlaceholderFormat(sq.Dollar).
		RunWith(tx).
		QueryRow().
		Scan(&id); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	// Batch the inserts of all the take questions.
	batch := sq.Insert(tableTakeAnswer).
		Columns(
			columnTakeAnswerTakeID,
			columnTakeAnswerAnswer,
			columnTakeAnswerCorrect,
			columnTakeAnswerQuestionID,
			columnTakeAnswerTimesSkipped,
			columnTakeAnswerSecondsElapsed,
		).
		PlaceholderFormat(sq.Dollar).
		RunWith(tx)
	for _, answer := range answers {
		batch = batch.Values(
			id,
			answer.Answer,
			answer.Correct,
			*answer.QuestionID,
			answer.TimesSkipped,
			answer.SecondsElapsed,
		)
	}

	// Execute the take question creation batch.
	if _, err = batch.Exec(); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	// If we got this far, the take now exists - so, commit the transaction.
	if err = tx.Commit(); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	return id, nil
}

// DeleteTake deletes a take from the db.
func DeleteTake(db *sql.DB, id int) error {
	// TODO(skeswa): remove all linked records.
	_, err := sq.Delete(tableTake).
		Where(sq.Eq{columnTakeID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectTake gets all the information about a specific take.
func SelectTake(db *sql.DB, id int) (*Take, error) {
	// Get the take itself.
	rows, err := sq.Select().
		Columns(columnTakeQuizID, columnTakeDateTaken).
		From(tableTake).
		Where(sq.Eq{columnTakeID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query()
	if err != nil {
		return nil, err
	}

	var (
		take   = Take{ID: id}
		quizID int
	)

	// Load the results into memory.
	if rows.Next() {
		rows.Scan(&quizID, &take.DateTaken)
		rows.Close()
	} else if err := rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	// Load the quiz.
	if take.Quiz, err = SelectQuiz(db, quizID); err != nil {
		return nil, err
	}

	// Load all of the the questions in this take.
	if rows, err = sq.Select().
		Columns(
			column(tableTakeAnswer, columnTakeAnswerID),
			column(tableTakeAnswer, columnTakeAnswerAnswer),
			column(tableTakeAnswer, columnTakeAnswerCorrect),
			column(tableTakeAnswer, columnTakeAnswerTimesSkipped),
			column(tableTakeAnswer, columnTakeAnswerSecondsElapsed),
			column(tableQuestion, columnQuestionID),
			column(tableQuestion, columnQuestionAnswer),
			column(tableQuestion, columnQuestionDateCreated),
			column(tableQuestion, columnQuestionAnswerPicture),
			column(tableQuestion, columnQuestionMultipleChoice),
			column(tableQuestion, columnQuestionQuestionPicture),
			column(tableCategory, columnCategoryID),
			column(tableCategory, columnCategoryName),
			column(tableDifficulty, columnDifficultyID),
			column(tableDifficulty, columnDifficultyName),
			column(tableDifficulty, columnDifficultyColor),
		).
		From(tableTakeAnswer).
		Where(sq.Eq{column(tableTakeAnswer, columnTakeAnswerTakeID): id}).
		Join(on(
			tableQuestion,
			column(tableQuestion, columnQuestionID),
			column(tableTakeAnswer, columnTakeAnswerQuestionID),
		)).
		Join(on(
			tableCategory,
			column(tableCategory, columnCategoryID),
			column(tableQuestion, columnQuestionCategoryID),
		)).
		Join(on(
			tableDifficulty,
			column(tableDifficulty, columnDifficultyID),
			column(tableQuestion, columnQuestionDifficultyID),
		)).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query(); err != nil {
		return nil, err
	}

	// Create each question in memory.
	answers := []*TakeAnswer{}
	for rows.Next() {
		var (
			category   = Category{}
			difficulty = Difficulty{}
			question   = Question{
				Category:   &category,
				Difficulty: &difficulty,
			}
			answer = TakeAnswer{
				Question: &question,
			}
		)

		err = rows.Scan(
			&answer.ID,
			&answer.Answer,
			&answer.Correct,
			&answer.TimesSkipped,
			&answer.SecondsElapsed,
			&question.ID,
			&question.Answer,
			&question.DateCreated,
			&question.AnswerPicture,
			&question.MultipleChoice,
			&question.QuestionPicture,
			&category.ID,
			&category.Name,
			&difficulty.ID,
			&difficulty.Name,
			&difficulty.Color,
		)
		if err != nil {
			return nil, err
		}

		answers = append(answers, &answer)
	}

	// Exit if there is a problem reading the rows.
	if err = rows.Err(); err != nil {
		return nil, err
	}

	// Attach the questions to the take, and then return.
	take.Answers = answers

	return &take, nil
}

// SelectTakes gets all takes from the db.
func SelectTakes(db *sql.DB) (Takes, error) {
	var (
		err  error
		rows *sql.Rows

		takes = []*Take{}
	)

	if rows, err = sq.Select().
		Columns(
			column(tableTake, columnTakeID),
			column(tableTake, columnTakeDateTaken),
			column(tableQuiz, columnQuizID),
			column(tableQuiz, columnQuizName),
			column(tableQuiz, columnQuizDescription),
			column(tableQuiz, columnQuizDateCreated),
		).
		From(tableTake).
		Join(on(
			tableQuiz,
			column(tableQuiz, columnQuizID),
			column(tableTake, columnTakeQuizID),
		)).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query(); err != nil {
		return nil, err
	}

	for rows.Next() {
		quiz := Quiz{}
		take := Take{
			Quiz: &quiz,
		}

		if err = rows.Scan(
			&take.ID,
			&take.DateTaken,
			&quiz.ID,
			&quiz.Name,
			&quiz.Description,
			&quiz.DateCreated,
		); err != nil {
			return nil, err
		}

		takes = append(takes, &take)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return takes, nil
}
