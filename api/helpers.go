package main

import (
	"net/http"

	"github.com/skeswa/gauntlet/api/dto"
)

func respondWithError(w http.ResponseWriter, code int, message string) {
	// Serialize the error message.
	errorMessage := &dto.Error{Message: message}
	serializedErrorMessage, err := errorMessage.MarshalJSON()
	if err != nil {
		panic(err)
	}

	// Write a response.
	http.Error(w, string(serializedErrorMessage), code)
}

func respondWithSuccess(w http.ResponseWriter, data []byte) {
	// Write a response.
	w.Header().Set("Content-Type", "application/json")
	w.Write(data)
}
