package model

import (
	"database/sql"
	"time"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableQuestion                    = "gauntlet.question"
	columnQuestionID                 = "id"
	columnQuestionAnswer             = "answer"
	columnQuestionSource             = "source"
	columnQuestionSourcePage         = "source_page"
	columnQuestionCategoryID         = "category_id"
	columnQuestionDateCreated        = "date_created"
	columnQuestionDifficultyID       = "difficulty_id"
	columnQuestionAnswerPicture      = "answer_picture"
	columnQuestionMultipleChoice     = "multiple_choice"
	columnQuestionQuestionPicture    = "question_picture"
	columnQuestionRequiresCalculator = "requires_calculator"
)

// Question is the model used for questions.
type Question struct {
	ID                 int
	Answer             string
	Source             sql.NullString
	Category           *Category
	SourcePage         sql.NullInt64
	Difficulty         *Difficulty
	DateCreated        time.Time
	AnswerPicture      string
	MultipleChoice     bool
	QuestionPicture    string
	RequiresCalculator bool
}

// ToDTO turns this model into a DTO.
func (q *Question) ToDTO() *dto.Question {
	var (
		source     *string
		category   *dto.Category
		sourcePage *int
		difficulty *dto.Difficulty
	)

	if q.Category != nil {
		category = q.Category.ToDTO()
	}
	if q.Difficulty != nil {
		difficulty = q.Difficulty.ToDTO()
	}
	if q.Source.Valid {
		source = &q.Source.String
	}
	if q.SourcePage.Valid {
		sourcePageValue := int(q.SourcePage.Int64)
		sourcePage = &sourcePageValue
	}

	return &dto.Question{
		ID:                 &q.ID,
		Answer:             q.Answer,
		Source:             source,
		Category:           category,
		SourcePage:         sourcePage,
		Difficulty:         difficulty,
		DateCreated:        &q.DateCreated,
		AnswerPicture:      q.AnswerPicture,
		MultipleChoice:     q.MultipleChoice,
		QuestionPicture:    q.QuestionPicture,
		RequiresCalculator: q.RequiresCalculator,
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
	source *string,
	sourcePage *int,
	categoryID int,
	difficultyID int,
	answerPicture string,
	multipleChoice bool,
	questionPicture string,
	requiresCalculator bool,
) (int, error) {
	var (
		id  int
		err error

		sourceValue     interface{}
		sourcePageValue interface{}
	)

	if source != nil {
		sourceValue = *source
	}
	if sourcePage != nil {
		sourcePageValue = *sourcePage
	}

	err = sq.Insert(tableQuestion).
		Columns(
			columnQuestionAnswer,
			columnQuestionSource,
			columnQuestionSourcePage,
			columnQuestionCategoryID,
			columnQuestionDateCreated,
			columnQuestionDifficultyID,
			columnQuestionAnswerPicture,
			columnQuestionMultipleChoice,
			columnQuestionQuestionPicture,
			columnQuestionRequiresCalculator,
		).
		Values(
			answer,
			sourceValue,
			sourcePageValue,
			categoryID,
			sqlNow,
			difficultyID,
			answerPicture,
			multipleChoice,
			questionPicture,
			requiresCalculator,
		).
		Suffix(returningIDClause).
		PlaceholderFormat(sq.Dollar).
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
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectQuestion get a difficulty from the db.
func SelectQuestion(db *sql.DB, id int) (*Question, error) {
	var (
		err  error
		rows *sql.Rows

		category   = Category{}
		difficulty = Difficulty{}
		question   = Question{
			Category:   &category,
			Difficulty: &difficulty,
		}
	)

	if rows, err = sq.Select().
		Columns(
			column(tableQuestion, columnQuestionID),
			column(tableQuestion, columnQuestionSource),
			column(tableQuestion, columnQuestionAnswer),
			column(tableQuestion, columnQuestionSourcePage),
			column(tableQuestion, columnQuestionDateCreated),
			column(tableQuestion, columnQuestionAnswerPicture),
			column(tableQuestion, columnQuestionMultipleChoice),
			column(tableQuestion, columnQuestionQuestionPicture),
			column(tableQuestion, columnQuestionRequiresCalculator),
			column(tableCategory, columnCategoryID),
			column(tableCategory, columnCategoryName),
			column(tableDifficulty, columnDifficultyID),
			column(tableDifficulty, columnDifficultyName),
			column(tableDifficulty, columnDifficultyColor),
		).
		From(tableQuestion).
		Where(sq.Eq{column(tableQuestion, columnQuestionID): id}).
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

	defer rows.Close()

	if rows.Next() {
		if err = rows.Scan(
			&question.ID,
			&question.Source,
			&question.Answer,
			&question.SourcePage,
			&question.DateCreated,
			&question.AnswerPicture,
			&question.MultipleChoice,
			&question.QuestionPicture,
			&question.RequiresCalculator,
			&category.ID,
			&category.Name,
			&difficulty.ID,
			&difficulty.Name,
			&difficulty.Color,
		); err != nil {
			return nil, err
		}
	} else if err = rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	return &question, nil
}

// SelectQuestions gets all questions from the db.
func SelectQuestions(db *sql.DB) (Questions, error) {
	var (
		err  error
		rows *sql.Rows

		questions = []*Question{}
	)

	if rows, err = sq.Select().
		Columns(
			column(tableQuestion, columnQuestionID),
			column(tableQuestion, columnQuestionSource),
			column(tableQuestion, columnQuestionAnswer),
			column(tableQuestion, columnQuestionSourcePage),
			column(tableQuestion, columnQuestionDateCreated),
			column(tableQuestion, columnQuestionAnswerPicture),
			column(tableQuestion, columnQuestionMultipleChoice),
			column(tableQuestion, columnQuestionQuestionPicture),
			column(tableQuestion, columnQuestionRequiresCalculator),
			column(tableCategory, columnCategoryID),
			column(tableCategory, columnCategoryName),
			column(tableDifficulty, columnDifficultyID),
			column(tableDifficulty, columnDifficultyName),
			column(tableDifficulty, columnDifficultyColor),
		).
		From(tableQuestion).
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
		Query(); err != nil {
		return nil, err
	}

	defer rows.Close()

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
			&question.Source,
			&question.Answer,
			&question.SourcePage,
			&question.DateCreated,
			&question.AnswerPicture,
			&question.MultipleChoice,
			&question.QuestionPicture,
			&question.RequiresCalculator,
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
