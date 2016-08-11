package main

import "fmt"

const (
	errorInvalidJSONPayload = "Received invalid JSON payload."
)

func errorInvalidJSONPayloadField(field string) string {
	return fmt.Sprintf(`%s Invalid value found for field "%s".`, errorInvalidJSONPayload, field)
}

func errorInvalidRouteVar(routeVar string) string {
	return fmt.Sprintf(`Invalid value found for route variable "%s".`, routeVar)
}

func errorNoSuchRecord(id int) string {
	return fmt.Sprintf(`Could not find record with id "%d".`, id)
}
