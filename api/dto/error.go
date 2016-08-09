package dto

//go:generate ffjson $GOFILE

// Error is the DTO used for errors.
type Error struct {
	Message string `json:"error"`
}
