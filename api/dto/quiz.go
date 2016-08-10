package dto

import "time"

//go:generate ffjson $GOFILE

// Quiz is the DTO used for quizzes.
type Quiz struct {
	ID          *int        `json:"id,omitempty"`
	Name        string      `json:"name"`
	Questions   []*Question `json:"questions,omitempty"`
	QuestionIDs []int       `json:"questionIds,omitempty"`
	Description string      `json:"description"`
	DateCreated *time.Time  `json:"dateCreated,omitempty"`
}
