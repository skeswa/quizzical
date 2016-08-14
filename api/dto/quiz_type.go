package dto

//go:generate ffjson $GOFILE

// QuizType is the DTO used for quiz types.
type QuizType struct {
	ID   *int   `json:"id,omitempty"`
	Name string `json:"name"`
}
