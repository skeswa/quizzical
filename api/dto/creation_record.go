package dto

//go:generate ffjson $GOFILE

// CreationRecord is the DTO used for creation records.
type CreationRecord struct {
	CreatedRecordID int `json:"createdRecordId"`
}
