package model

import (
	"database/sql"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableQuizType      = "gauntlet.quiz_type"
	columnQuizTypeID   = "id"
	columnQuizTypeName = "name"
)

// QuizType is the model used for quizTypes.
type QuizType struct {
	ID   int
	Name string
}

// ToDTO turns this model into a DTO.
func (c *QuizType) ToDTO() *dto.QuizType {
	return &dto.QuizType{
		ID:   &c.ID,
		Name: c.Name,
	}
}

// QuizTypes is a collection of quizType models.
type QuizTypes []*QuizType

// ToDTO turns these models into DTOs.
func (c QuizTypes) ToDTO() []*dto.QuizType {
	dtos := []*dto.QuizType{}

	// Turn each model into a DTO.
	for _, quizType := range c {
		dtos = append(dtos, quizType.ToDTO())
	}

	return dtos
}

// InsertQuizType inserts a quizType into the db.
func InsertQuizType(db *sql.DB, name string) (int, error) {
	var (
		id  int
		err error
	)

	err = sq.Insert(tableQuizType).
		Columns(columnQuizTypeName).
		Values(name).
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

// DeleteQuizType deletes a quizType from the db.
func DeleteQuizType(db *sql.DB, id int) error {
	// TODO(skeswa): remove replace all linked records with null.
	_, err := sq.Delete(tableQuizType).
		Where(sq.Eq{columnQuizTypeID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectQuizType get a quizType from the db.
func SelectQuizType(db *sql.DB, id int) (*QuizType, error) {
	var (
		err  error
		rows *sql.Rows

		quizType = QuizType{}
	)

	if rows, err = sq.Select(columnQuizTypeID, columnQuizTypeName).
		From(tableQuizType).
		Where(sq.Eq{columnQuizTypeID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query(); err != nil {
		return nil, err
	}

	if rows.Next() {
		if err = rows.Scan(&quizType.ID, &quizType.Name); err != nil {
			return nil, err
		}
	} else if err = rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	return &quizType, nil
}

// SelectQuizTypes gets all quizTypes from the db.
func SelectQuizTypes(db *sql.DB) (QuizTypes, error) {
	var (
		err  error
		rows *sql.Rows

		quizTypes = []*QuizType{}
	)

	rows, err = sq.Select(columnQuizTypeID, columnQuizTypeName).
		From(tableQuizType).
		RunWith(db).
		Query()

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		quizType := QuizType{}

		err = rows.Scan(&quizType.ID, &quizType.Name)
		if err != nil {
			return nil, err
		}

		quizTypes = append(quizTypes, &quizType)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return quizTypes, nil
}
