package dto

import "time"

//go:generate ffjson $GOFILE

// Question is the DTO used for categories.
type Question struct {
	ID             *int        `json:"id,omitempty"`
	Answer         string      `json:"answer"`
	Picture        string      `json:"picture"`
	Category       *Category   `json:"category,omitempty"`
	CategoryID     *int        `json:"categoryId,omitempty"`
	Difficulty     *Difficulty `json:"difficulty,omitempty"`
	DateCreated    *time.Time  `json:"dateCreated,omitempty"`
	DifficultyID   *int        `json:"difficultyId,omitempty"`
	MultipleChoice bool        `json:"multipleChoice"`
}
