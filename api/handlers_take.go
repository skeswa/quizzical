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
	routeVarTakeID          = "id"
	takePayloadFieldQuizID  = "quizId"
	takePayloadFieldAnswers = "answers"
)

func createTakeHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Take
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if payload.QuizID == nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(takePayloadFieldQuizID))
			return
		} else if len(payload.Answers) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(takePayloadFieldAnswers))
			return
		} else {
			for _, answer := range payload.Answers {
				if answer.QuestionID == nil {
					respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(takePayloadFieldAnswers))
					return
				} else if len(answer.Answer) < 1 {
					respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(takePayloadFieldAnswers))
					return
				}
			}
		}

		// Interface with the database.
		id, err := model.InsertTake(db, *payload.QuizID, payload.Answers)
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

func deleteTakeHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the take id.
		idStr := mux.Vars(r)[routeVarTakeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarTakeID))
			return
		}

		// Interface with the database.
		err = model.DeleteTake(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, nil)
		return
	}
}

func getTakesHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		takes, err := model.SelectTakes(db)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(takes.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func getTakeHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the take id.
		idStr := mux.Vars(r)[routeVarTakeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarTakeID))
			return
		}

		// Interface with the database.
		take, err := model.SelectTake(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if take == nil {
			// If the take is nil, then there is none.
			respondWithError(w, http.StatusBadRequest, errorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(take.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}
