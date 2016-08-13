package dto

//go:generate ffjson $GOFILE

// DeletionRecord is the DTO used for deletion records.
type DeletionRecord struct {
	DeletedRecordID int `json:"deletedRecordId"`
}
