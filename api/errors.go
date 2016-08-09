package main

import "fmt"

const (
	errorInvalidIDParam     = "Received invalid id parameter."
	errorInvalidJSONPayload = "Received invalid JSON payload."
)

func errorInvalidJSONPayloadField(field string) string {
	return fmt.Sprintf(`%s Invalid value found for field "%s".`, errorInvalidJSONPayload, field)
}
