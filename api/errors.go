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

func errorNoSuchEntity(id int) string {
	return fmt.Sprintf(`No such entity with id "%d".`, id)
}
