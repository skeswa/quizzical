package dto

//go:generate ffjson $GOFILE

// Difficulty is the DTO used for categories.
type Difficulty struct {
	ID    *int   `json:"id,omitempty"`
	Name  string `json:"name"`
	Color string `json:"color"`
}
