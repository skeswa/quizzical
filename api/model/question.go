package model

import (
	"database/sql"
	"time"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableQuestion                = "gauntlet.question"
	columnQuestionID             = "id"
	columnQuestionAnswer         = "answer"
	columnQuestionPicture        = "picture"
	columnQuestionCategoryID     = "category_id"
	columnQuestionDateCreated    = "date_created"
	columnQuestionDifficultyID   = "difficulty_id"
	columnQuestionMultipleChoice = "multiple_choice"
)

// Question is the model used for questions.
type Question struct {
	ID             int
	Answer         string
	Picture        string
	Category       *Category
	Difficulty     *Difficulty
	DateCreated    time.Time
	MultipleChoice bool
}

// ToDTO turns this model into a DTO.
func (q *Question) ToDTO() *dto.Question {
	var (
		category   *dto.Category
		difficulty *dto.Difficulty
	)

	if q.Category != nil {
		category = q.Category.ToDTO()
	}

	if q.Difficulty != nil {
		difficulty = q.Difficulty.ToDTO()
	}

	return &dto.Question{
		ID:             &q.ID,
		Answer:         q.Answer,
		Picture:        q.Picture,
		Category:       category,
		Difficulty:     difficulty,
		DateCreated:    q.DateCreated,
		MultipleChoice: q.MultipleChoice,
	}
}

// Questions is a collection of question models.
type Questions []*Question

// ToDTO turns these models into DTOs.
func (q Questions) ToDTO() []*dto.Question {
	dtos := []*dto.Question{}

	// Turn each model into a DTO.
	for _, question := range q {
		dtos = append(dtos, question.ToDTO())
	}

	return dtos
}

// InsertQuestion inserts a question into the db.
func InsertQuestion(
	db *sql.DB,
	answer string,
	picture string,
	categoryID int,
	difficultyID int,
	multipleChoice bool,
) (int, error) {
	var (
		id  int
		err error
	)

	err = sq.Insert(tableQuestion).
		Columns(
			columnQuestionAnswer,
			columnQuestionPicture,
			columnQuestionCategoryID,
			columnQuestionDateCreated,
			columnQuestionDifficultyID,
			columnQuestionMultipleChoice,
		).
		Values(
			answer,
			picture,
			categoryID,
			sqlNow,
			difficultyID,
			multipleChoice,
		).
		Suffix(returningIDClause).
		RunWith(db).
		QueryRow().
		Scan(&id)

	if err != nil {
		return -1, err
	}

	return id, nil
}

// DeleteQuestion deletes a question from the db.
func DeleteQuestion(db *sql.DB, id int) error {
	// TODO(skeswa): remove replace all linked records with null.
	_, err := sq.Delete(tableQuestion).
		Where(sq.Eq{columnQuestionID: id}).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectQuestions gets all questions from the db.
func SelectQuestions(db *sql.DB) (Questions, error) {
	var (
		err  error
		rows *sql.Rows

		questions = []*Question{}
	)

	rows, err = sq.Select(tableQuestion).
		Columns(
			column(tableQuestion, columnQuestionID),
			column(tableQuestion, columnQuestionAnswer),
			column(tableQuestion, columnQuestionPicture),
			column(tableQuestion, columnQuestionDateCreated),
			column(tableQuestion, columnQuestionMultipleChoice),
			column(tableCategory, columnCategoryID),
			column(tableCategory, columnCategoryName),
			column(tableDifficulty, columnDifficultyID),
			column(tableDifficulty, columnDifficultyName),
			column(tableDifficulty, columnDifficultyColor),
		).
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
		RunWith(db).
		Query()

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		var (
			category   = Category{}
			difficulty = Difficulty{}
			question   = Question{
				Category:   &category,
				Difficulty: &difficulty,
			}
		)

		err = rows.Scan(
			&question.ID,
			&question.Answer,
			&question.Picture,
			&question.DateCreated,
			&question.MultipleChoice,
			&category.ID,
			&category.Name,
			&difficulty.ID,
			&difficulty.Name,
			&difficulty.Color,
		)
		if err != nil {
			return nil, err
		}

		questions = append(questions, &question)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return questions, nil
}
