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
	routeVarQuizID              = "id"
	quizPayloadFieldName        = "name"
	quizPayloadFieldQuizTypeID  = "typeId"
	quizPayloadFieldDescription = "description"
	quizPayloadFieldQuestionIDs = "questionIds"
)

func createQuizHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read body.
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Read payload.
		var payload dto.Quiz
		err = ffjson.Unmarshal(body, &payload)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayload)
			return
		}

		// Validate payload.
		if len(payload.Name) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(quizPayloadFieldName))
			return
		} else if len(payload.Description) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(quizPayloadFieldDescription))
			return
		} else if len(payload.QuestionIDs) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(quizPayloadFieldQuestionIDs))
			return
		} else if payload.TypeID == nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(quizPayloadFieldQuizTypeID))
			return
		}

		// Interface with the database.
		id, err := model.InsertQuiz(db, payload.Name, *payload.TypeID, payload.Description, payload.QuestionIDs)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.MarshalFast(&dto.CreationRecord{CreatedRecordID: id})
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func deleteQuizHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarQuizID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarQuizID))
			return
		}

		// Interface with the database.
		err = model.DeleteQuiz(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, nil)
		return
	}
}

func getQuizzesHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		quizzes, err := model.SelectQuizzes(db)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(quizzes.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func getQuizHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarQuizID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarQuizID))
			return
		}

		// Interface with the database.
		quiz, err := model.SelectQuiz(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if quiz == nil {
			// If the quiz is nil, then there is none.
			respondWithError(w, http.StatusBadRequest, errorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(quiz.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}
