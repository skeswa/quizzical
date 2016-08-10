package dto

import "time"

//go:generate ffjson $GOFILE

// Take is the DTO used for takes.
type Take struct {
	ID        *int         `json:"id,omitempty"`
	Quiz      *Quiz        `json:"quiz,omitempty"`
	QuizID    *int         `json:"quizId,omitempty"`
	Answers   []TakeAnswer `json:"answers,omitempty"`
	DateTaken *time.Time   `json:"dateTaken,omitempty"`
}

// TakeAnswer is the DTO used for take answers.
type TakeAnswer struct {
	ID             *int      `json:"id,omitempty"`
	TakeID         *int      `json:"takeId,omitempty"`
	Answer         string    `json:"answer"`
	Correct        bool      `json:"correct"`
	Question       *Question `json:"question,omitempty"`
	QuestionID     *int      `json:"questionId,omitempty"`
	TimesSkipped   int       `json:"timesSkipped"`
	SecondsElapsed int       `json:"secondsElapsed"`
}
