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
	routeVarQuizTypeID         = "id"
	queryStringVarQuizTypeName = "name"
)

func CreateQuizType(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.QuizType
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if len(payload.Name) < 1 {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField("name"))
			return
		}

		// Interface with the database.
		id, err := model.InsertQuizType(db, payload.Name)
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

func DeleteQuizType(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quizType id.
		idStr := mux.Vars(r)[routeVarQuizTypeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarQuizTypeID))
			return
		}

		// Interface with the database.
		err = model.DeleteQuizType(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, nil)
		return
	}
}

func GetQuizTypes(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		quizTypes, err := model.SelectQuizTypes(db)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(quizTypes.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func GetQuizType(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarQuizTypeID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarQuizTypeID))
			return
		}

		// Interface with the database.
		quizType, err := model.SelectQuizType(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if quizType == nil {
			// If the quiz is nil, then there is none.
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(quizType.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func ProvideQuizType(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		name := r.URL.Query().Get(queryStringVarQuizTypeName)
		if len(name) < 1 {
			helpers.RespondWithError(
				w,
				http.StatusBadRequest,
				helpers.ErrorInvalidQueryStringVar(queryStringVarQuizTypeName),
			)
			return
		}

		// Interface with the database.
		quizType, err := model.ProvideQuizType(db, name)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(quizType.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}
