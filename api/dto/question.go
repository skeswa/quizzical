package dto

import "time"

//go:generate ffjson $GOFILE

// Question is the DTO used for categories.
type Question struct {
	ID                 *int        `json:"id,omitempty"`
	Answer             string      `json:"answer"`
	Source             *string     `json:"source"`
	Category           *Category   `json:"category,omitempty"`
	SourcePage         *int        `json:"sourcePage"`
	CategoryID         *int        `json:"categoryId,omitempty"`
	Difficulty         *Difficulty `json:"difficulty,omitempty"`
	DateCreated        *time.Time  `json:"dateCreated,omitempty"`
	IndexInPage        *int        `json:"indexInPage,omitempty"`
	DifficultyID       *int        `json:"difficultyId,omitempty"`
	AnswerPicture      string      `json:"answerPicture"`
	MultipleChoice     bool        `json:"multipleChoice"`
	QuestionPicture    string      `json:"questionPicture"`
	RequiresCalculator bool        `json:"requiresCalculator"`
}
