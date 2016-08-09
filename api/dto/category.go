package dto

//go:generate ffjson $GOFILE

// Category is the DTO used for categories.
type Category struct {
	ID   *int   `json:"id,omitempty"`
	Name string `json:"name"`
}
