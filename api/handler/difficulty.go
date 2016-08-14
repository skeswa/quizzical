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
	routeVarDifficultyID          = "id"
	queryStringVarDifficultyName  = "name"
	queryStringVarDifficultyColor = "color"
)

func CreateDifficulty(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Difficulty
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if len(payload.Name) < 1 {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField("name"))
			return
		} else if len(payload.Color) < 1 {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayloadField("color"))
			return
		}

		// Interface with the database.
		id, err := model.InsertDifficulty(db, payload.Name, payload.Color)
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

func DeleteDifficulty(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the difficulty id.
		idStr := mux.Vars(r)[routeVarDifficultyID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarDifficultyID))
			return
		}

		// Interface with the database.
		err = model.DeleteDifficulty(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, nil)
		return
	}
}

func GetDifficulties(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		difficulties, err := model.SelectDifficulties(db)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(difficulties.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func GetDifficulty(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarDifficultyID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarDifficultyID))
			return
		}

		// Interface with the database.
		difficulty, err := model.SelectDifficulty(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if difficulty == nil {
			// If the quiz is nil, then there is none.
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(difficulty.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func ProvideDifficulty(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		name := r.URL.Query().Get(queryStringVarDifficultyName)
		if len(name) < 1 {
			helpers.RespondWithError(
				w,
				http.StatusBadRequest,
				helpers.ErrorInvalidQueryStringVar(queryStringVarDifficultyName),
			)
			return
		}
		color := r.URL.Query().Get(queryStringVarDifficultyColor)
		if len(color) < 1 {
			helpers.RespondWithError(
				w,
				http.StatusBadRequest,
				helpers.ErrorInvalidQueryStringVar(queryStringVarDifficultyColor),
			)
			return
		}

		// Interface with the database.
		difficulty, err := model.ProvideDifficulty(db, name, color)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(difficulty.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}
