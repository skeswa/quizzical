package model

import (
	"database/sql"

	"github.com/skeswa/gauntlet/api/dto"
	sq "gopkg.in/Masterminds/squirrel.v1"
)

const (
	tableCategory      = "gauntlet.question_category"
	columnCategoryID   = "id"
	columnCategoryName = "name"
)

// Category is the model used for categories.
type Category struct {
	ID   int
	Name string
}

// ToDTO turns this model into a DTO.
func (c *Category) ToDTO() *dto.Category {
	return &dto.Category{
		ID:   &c.ID,
		Name: c.Name,
	}
}

// Categories is a collection of category models.
type Categories []*Category

// ToDTO turns these models into DTOs.
func (c Categories) ToDTO() []*dto.Category {
	dtos := []*dto.Category{}

	// Turn each model into a DTO.
	for _, category := range c {
		dtos = append(dtos, category.ToDTO())
	}

	return dtos
}

// InsertCategory inserts a category into the db.
func InsertCategory(db *sql.DB, name string) (int, error) {
	var (
		id  int
		err error
	)

	err = sq.Insert(tableCategory).
		Columns(columnCategoryName).
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

// DeleteCategory deletes a category from the db.
func DeleteCategory(db *sql.DB, id int) error {
	// TODO(skeswa): remove replace all linked records with null.
	_, err := sq.Delete(tableCategory).
		Where(sq.Eq{columnCategoryID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Exec()

	if err != nil {
		return err
	}

	return nil
}

// SelectCategory get a category from the db.
func SelectCategory(db *sql.DB, id int) (*Category, error) {
	var (
		err  error
		rows *sql.Rows

		category = Category{}
	)

	if rows, err = sq.Select(columnCategoryID, columnCategoryName).
		From(tableCategory).
		Where(sq.Eq{columnCategoryID: id}).
		PlaceholderFormat(sq.Dollar).
		RunWith(db).
		Query(); err != nil {
		return nil, err
	}

	if rows.Next() {
		if err = rows.Scan(&category.ID, &category.Name); err != nil {
			return nil, err
		}
	} else if err = rows.Err(); err != nil {
		return nil, err
	} else {
		return nil, nil
	}

	return &category, nil
}

// SelectCategories gets all categories from the db.
func SelectCategories(db *sql.DB) (Categories, error) {
	var (
		err  error
		rows *sql.Rows

		categories = []*Category{}
	)

	rows, err = sq.Select(columnCategoryID, columnCategoryName).
		From(tableCategory).
		RunWith(db).
		Query()

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		category := Category{}

		err = rows.Scan(&category.ID, &category.Name)
		if err != nil {
			return nil, err
		}

		categories = append(categories, &category)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return categories, nil
}
