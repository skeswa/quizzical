package dto

import "time"

//go:generate ffjson $GOFILE

// Quiz is the DTO used for quizzes.
type Quiz struct {
	ID          *int        `json:"id,omitempty"`
	Name        string      `json:"name"`
	Type        *QuizType   `json:"type,omitempty"`
	TypeID      *int        `json:"typeId,omitempty"`
	Questions   []*Question `json:"questions,omitempty"`
	QuestionIDs []int       `json:"questionIds,omitempty"`
	Description string      `json:"description"`
	DateCreated *time.Time  `json:"dateCreated,omitempty"`
}
