package model

import (
	"database/sql"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableDifficulty       = "gauntlet.question_difficulty"
	columnDifficultyID    = "id"
	columnDifficultyName  = "name"
	columnDifficultyColor = "color"
)

// Difficulty is the model used for difficulties.
type Difficulty struct {
	ID    int
	Name  string
	Color string
}

// ToDTO turns this model into a DTO.
func (d *Difficulty) ToDTO() *dto.Difficulty {
	return &dto.Difficulty{
		ID:    &d.ID,
		Name:  d.Name,
		Color: d.Color,
	}
}

// Difficulties is a collection of difficulty models.
type Difficulties []*Difficulty

// ToDTO turns these models into DTOs.
func (d Difficulties) ToDTO() []*dto.Difficulty {
	dtos := []*dto.Difficulty{}

	// Turn each model into a DTO.
	for _, difficulty := range d {
		dtos = append(dtos, difficulty.ToDTO())
	}

	return dtos
}

// InsertDifficulty inserts a difficulty into the db.
func InsertDifficulty(db *sql.DB, name string, color string) (int, error) {
	var (
		id  int
		err error
	)

	err = sq.Insert(tableDifficulty).
		Columns(columnDifficultyName, columnDifficultyColor).
		Values(name, color).
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

// DeleteDifficulty deletes a difficulty from the db.
func DeleteDifficulty(db *sql.DB, id int) error {
	// TODO(skeswa): remove replace all linked records with null.
	_, err := sq.Delete(tableDifficulty).
		Where(sq.Eq{columnDifficultyID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectDifficulty get a difficulty from the db.
func SelectDifficulty(db *sql.DB, id int) (*Difficulty, error) {
	var (
		err  error
		rows *sql.Rows

		difficulty = Difficulty{}
	)

	if rows, err = sq.Select(columnDifficultyID, columnDifficultyName, columnDifficultyColor).
		From(tableDifficulty).
		Where(sq.Eq{columnDifficultyID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query(); err != nil {
		return nil, err
	}

	if rows.Next() {
		if err = rows.Scan(&difficulty.ID, &difficulty.Name, &difficulty.Color); err != nil {
			return nil, err
		}
	} else if err = rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	return &difficulty, nil
}

// SelectDifficulties gets all difficulties from the db.
func SelectDifficulties(db *sql.DB) (Difficulties, error) {
	var (
		err  error
		rows *sql.Rows

		difficulties = []*Difficulty{}
	)

	rows, err = sq.Select(columnDifficultyID, columnDifficultyName, columnDifficultyColor).
		From(tableDifficulty).
		RunWith(db).
		Query()

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		difficulty := Difficulty{}

		err = rows.Scan(&difficulty.ID, &difficulty.Name, &difficulty.Color)
		if err != nil {
			return nil, err
		}

		difficulties = append(difficulties, &difficulty)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return difficulties, nil
}
