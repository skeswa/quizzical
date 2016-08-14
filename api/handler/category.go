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
	routeVarCategoryID = "id"
)

func CreateCategory(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Category
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
		id, err := model.InsertCategory(db, payload.Name)
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

func DeleteCategory(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the category id.
		idStr := mux.Vars(r)[routeVarCategoryID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarCategoryID))
			return
		}

		// Interface with the database.
		err = model.DeleteCategory(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, nil)
		return
	}
}

func GetCategories(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		categories, err := model.SelectCategories(db)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(categories.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}

func GetCategory(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarCategoryID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarCategoryID))
			return
		}

		// Interface with the database.
		category, err := model.SelectCategory(db, id)
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if category == nil {
			// If the quiz is nil, then there is none.
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(category.ToDTO())
		if err != nil {
			helpers.RespondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		helpers.RespondWithSuccess(w, response)
		return
	}
}
