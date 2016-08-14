package helpers

import "fmt"

const (
	ErrorInvalidJSONPayload = "Received invalid JSON payload."
)

func ErrorInvalidJSONPayloadField(field string) string {
	return fmt.Sprintf(`%s Invalid value found for field "%s".`, ErrorInvalidJSONPayload, field)
}

func ErrorInvalidRouteVar(routeVar string) string {
	return fmt.Sprintf(`Invalid value found for route variable "%s".`, routeVar)
}

func ErrorNoSuchRecord(id int) string {
	return fmt.Sprintf(`Could not find record with id "%d".`, id)
}
