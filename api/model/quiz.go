package model

import (
	"database/sql"
	"time"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableQuiz             = "gauntlet.quiz"
	columnQuizID          = "id"
	columnQuizName        = "name"
	columnQuizQuizTypeID  = "quiz_type_id"
	columnQuizDescription = "description"
	columnQuizDateCreated = "date_created"

	tableQuizQuestion            = "gauntlet.quiz_question"
	columnQuizQuestionID         = "id"
	columnQuizQuestionNumber     = "question_number"
	columnQuizQuestionQuizID     = "quiz_id"
	columnQuizQuestionQuestionID = "question_id"
)

// Quiz is the model used for quizzes.
type Quiz struct {
	ID          int
	Name        string
	Type        *QuizType
	Questions   []*Question
	Description string
	DateCreated time.Time
}

// ToDTO turns this model into a DTO.
func (d *Quiz) ToDTO() *dto.Quiz {
	var (
		quizType  *dto.QuizType
		questions []*dto.Question
	)

	if d.Questions != nil {
		questions = Questions(d.Questions).ToDTO()
	}
	if d.Type != nil {
		quizType = d.Type.ToDTO()
	}

	return &dto.Quiz{
		ID:          &d.ID,
		Name:        d.Name,
		Type:        quizType,
		Questions:   questions,
		Description: d.Description,
		DateCreated: &d.DateCreated,
	}
}

// Quizzes is a collection of quiz models.
type Quizzes []*Quiz

// ToDTO turns these models into DTOs.
func (d Quizzes) ToDTO() []*dto.Quiz {
	dtos := []*dto.Quiz{}

	// Turn each model into a DTO.
	for _, quiz := range d {
		dtos = append(dtos, quiz.ToDTO())
	}

	return dtos
}

// InsertQuiz inserts a quiz into the db.
func InsertQuiz(db *sql.DB, name string, quizTypeID int, description string, questionIDs []int) (int, error) {
	var (
		id  int
		err error
	)

	// Start the insertion transaction.
	tx, err := db.Begin()
	if err != nil {
		return -1, err
	}

	// Create the quiz first and foremost.
	if err = sq.Insert(tableQuiz).
		Columns(columnQuizName, columnQuizQuizTypeID, columnQuizDescription, columnQuizDateCreated).
		Values(name, quizTypeID, description, sqlNow).
		Suffix(returningIDClause).
		PlaceholderFormat(sq.Dollar).
		RunWith(tx).
		QueryRow().
		Scan(&id); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	// Batch the inserts of all the quiz questions.
	batch := sq.Insert(tableQuizQuestion).
		Columns(columnQuizQuestionNumber, columnQuizQuestionQuizID, columnQuizQuestionQuestionID).
		PlaceholderFormat(sq.Dollar).
		RunWith(tx)
	for i, questionID := range questionIDs {
		batch = batch.Values(i+1, id, questionID)
	}

	// Execute the quiz question creation batch.
	if _, err = batch.Exec(); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	// If we got this far, the quiz now exists - so, commit the transaction.
	if err = tx.Commit(); err != nil {
		_ = tx.Rollback()
		return -1, err
	}

	return id, nil
}

// DeleteQuiz deletes a quiz from the db.
func DeleteQuiz(db *sql.DB, id int) error {
	// TODO(skeswa): remove all linked records.
	_, err := sq.Delete(tableQuiz).
		Where(sq.Eq{columnQuizID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectQuiz gets all the information about a specific quiz.
func SelectQuiz(db *sql.DB, id int) (*Quiz, error) {
	// Get the quiz itself.
	rows, err := sq.Select().
		Columns(
			column(tableQuiz, columnQuizName),
			column(tableQuiz, columnQuizDescription),
			column(tableQuiz, columnQuestionDateCreated),
			column(tableQuizType, columnQuizTypeID),
			column(tableQuizType, columnQuizTypeName),
		).
		From(tableQuiz).
		Where(sq.Eq{columnQuizID: id}).
		Join(on(
			tableQuizType,
			column(tableQuiz, columnQuizQuizTypeID),
			column(tableQuizType, columnQuizTypeID),
		)).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query()
	if err != nil {
		return nil, err
	}

	// Load the results into memory.
	quiz := Quiz{ID: id, Type: &QuizType{}}
	if rows.Next() {
		rows.Scan(&quiz.Name, &quiz.Description, &quiz.DateCreated, &quiz.Type.ID, &quiz.Type.Name)
		rows.Close()
	} else if err := rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	// Load all of the the questions in this quiz.
	if rows, err = sq.Select().
		Columns(
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
		From(tableQuizQuestion).
		Where(sq.Eq{column(tableQuizQuestion, columnQuizQuestionQuizID): id}).
		OrderBy(column(tableQuizQuestion, columnQuizQuestionNumber)).
		Join(on(
			tableQuestion,
			column(tableQuestion, columnQuestionID),
			column(tableQuizQuestion, columnQuizQuestionQuestionID),
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
	questions := []*Question{}
	for rows.Next() {
		var (
			category   = Category{}
			difficulty = Difficulty{}
			question   = Question{
				Category:   &category,
				Difficulty: &difficulty,
			}
		)

		if err = rows.Scan(
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
		); err != nil {
			return nil, err
		}

		questions = append(questions, &question)
	}

	// Exit if there is a problem reading the rows.
	if err = rows.Err(); err != nil {
		return nil, err
	}

	// Attach the questions to the quiz, and then return.
	quiz.Questions = Questions(questions)

	return &quiz, nil
}

// SelectQuizzes gets all quizzes from the db.
func SelectQuizzes(db *sql.DB) (Quizzes, error) {
	var (
		err  error
		rows *sql.Rows

		quizzes = []*Quiz{}
	)

	rows, err = sq.Select().
		Columns(
			column(tableQuiz, columnQuizID),
			column(tableQuiz, columnQuizName),
			column(tableQuiz, columnQuizDescription),
			column(tableQuiz, columnQuestionDateCreated),
			column(tableQuizType, columnQuizTypeID),
			column(tableQuizType, columnQuizTypeName),
		).
		From(tableQuiz).
		Join(on(
			tableQuizType,
			column(tableQuiz, columnQuizQuizTypeID),
			column(tableQuizType, columnQuizTypeID),
		)).
		RunWith(db).
		Query()

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		quiz := Quiz{Type: &QuizType{}}

		if err = rows.Scan(
			&quiz.ID,
			&quiz.Name,
			&quiz.Description,
			&quiz.DateCreated,
			&quiz.Type.ID,
			&quiz.Type.Name,
		); err != nil {
			return nil, err
		}

		quizzes = append(quizzes, &quiz)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return quizzes, nil
}
