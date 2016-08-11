package main

import (
	"database/sql"
	"io/ioutil"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"github.com/pquerna/ffjson/ffjson"
	"github.com/skeswa/gauntlet/api/dto"
	"github.com/skeswa/gauntlet/api/model"
)

const (
	routeVarDifficultyID = "id"
)

func createDifficultyHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Difficulty
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if len(payload.Name) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField("name"))
			return
		} else if len(payload.Color) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField("color"))
			return
		}

		// Interface with the database.
		id, err := model.InsertDifficulty(db, payload.Name, payload.Color)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.MarshalFast(&dto.CreationRecord{NewRecordID: id})
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func deleteDifficultyHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the difficulty id.
		idStr := mux.Vars(r)[routeVarDifficultyID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarDifficultyID))
			return
		}

		// Interface with the database.
		err = model.DeleteDifficulty(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, nil)
		return
	}
}

func getDifficultiesHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		difficulties, err := model.SelectDifficulties(db)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(difficulties.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func getDifficultyHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarDifficultyID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarDifficultyID))
			return
		}

		// Interface with the database.
		difficulty, err := model.SelectDifficulty(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if difficulty == nil {
			// If the quiz is nil, then there is none.
			respondWithError(w, http.StatusBadRequest, errorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(difficulty.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}
