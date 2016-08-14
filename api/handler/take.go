package handler

import (
	"database/sql"
	"io/ioutil"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"github.com/pquerna/ffjson/ffjson"
	"github.com/skeswa/gauntlet/api/dto"
	"github.com/skeswa/gauntlet/api/handler/helpers"
	"github.com/skeswa/gauntlet/api/model"
)

const (
	routeVarTakeID          = "id"
	takePayloadFieldQuizID  = "quizId"
	takePayloadFieldAnswers = "answers"
)

func CreateTake(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Take
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if payload.QuizID == nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField(takePayloadFieldQuizID))
			return
		} else if len(payload.Answers) < 1 {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField(takePayloadFieldAnswers))
			return
		} else {
			for _, answer := range payload.Answers {
				if answer.QuestionID == nil {
					helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField(takePayloadFieldAnswers))
					return
				} else if len(answer.Answer) < 1 {
					helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField(takePayloadFieldAnswers))
					return
				}
			}
		}

		// Interface with the database.
		id, err := model.InsertTake(db, *payload.QuizID, payload.Answers)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.MarshalFast(&dto.CreationRecord{CreatedRecordID: id})
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func DeleteTake(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the take id.
		idStr := mux.Vars(r)[routeVarTakeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarTakeID))
			return
		}

		// Interface with the database.
		err = model.DeleteTake(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, nil)
		return
	}
}

func GetTakes(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		takes, err := model.SelectTakes(db)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(takes.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func GetTake(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the take id.
		idStr := mux.Vars(r)[routeVarTakeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarTakeID))
			return
		}

		// Interface with the database.
		take, err := model.SelectTake(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if take == nil {
			// If the take is nil, then there is none.
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(take.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}
