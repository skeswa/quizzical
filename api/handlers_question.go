package main

import (
	"database/sql"
	"io"
	"net/http"
	"os"
	"path/filepath"
	"strconv"

	"github.com/gorilla/mux"
	"github.com/pquerna/ffjson/ffjson"
	"github.com/satori/go.uuid"
	"github.com/skeswa/gauntlet/api/common"
	"github.com/skeswa/gauntlet/api/dto"
	"github.com/skeswa/gauntlet/api/model"
)

const (
	// maxPictureFileSize is the maximum picture size (2 MB).
	maxPictureFileSize              = 2 * 1024 * 1024
	routeVarQuestionID              = "id"
	questionFormFieldAnswer         = "answer"
	questionFormFieldPicture        = "picture"
	questionFormFieldQuestionID     = "questionId"
	questionFormFieldDifficultyID   = "difficultyId"
	questionFormFieldMultipleChoice = "multipleChoice"
)

func createQuestionHandler(db *sql.DB, c *common.Config) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the basic fields first.
		answer := r.FormValue(questionFormFieldAnswer)
		if len(answer) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldAnswer))
			return
		}
		questionIDStr := r.FormValue(questionFormFieldQuestionID)
		questionID, err := strconv.Atoi(questionIDStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldQuestionID))
			return
		}
		difficultyIDStr := r.FormValue(questionFormFieldDifficultyID)
		difficultyID, err := strconv.Atoi(difficultyIDStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldDifficultyID))
			return
		}
		multipleChoiceStr := r.FormValue(questionFormFieldMultipleChoice)
		multipleChoice, err := strconv.ParseBool(multipleChoiceStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldMultipleChoice))
			return
		}

		// Read the input picture file from the form.
		r.ParseMultipartForm(maxPictureFileSize)
		inputPictureFile, inputPictureFileHeader, err := r.FormFile(questionFormFieldPicture)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldPicture))
			return
		}

		// Close both files after the response is sent.
		defer inputPictureFile.Close()

		// Create the output picture file.
		inputPictureFileExt := filepath.Ext(inputPictureFileHeader.Filename)
		outputPictureFileName := uuid.NewV4().String() + inputPictureFileExt
		outputPictureFile, err := os.Create(filepath.Join(c.ImagesPath, outputPictureFileName))
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Close both files after the response is sent.
		defer outputPictureFile.Close()

		// Perform the copy from input to output.
		_, err = io.Copy(outputPictureFile, inputPictureFile)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Interface with the database.
		id, err := model.InsertQuestion(
			db,
			answer,
			outputPictureFileName,
			questionID,
			difficultyID,
			multipleChoice,
		)
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

func deleteQuestionHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the question id.
		idStr := mux.Vars(r)[routeVarQuestionID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarQuestionID))
			return
		}

		// Interface with the database.
		err = model.DeleteQuestion(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, nil)
		return
	}
}

func getQuestionsHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Interface with the database.
		questions, err := model.SelectQuestions(db)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(questions.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}

func getQuestionHandler(db *sql.DB) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the quiz id.
		idStr := mux.Vars(r)[routeVarQuestionID]
		id, err := strconv.Atoi(idStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarQuestionID))
			return
		}

		// Interface with the database.
		question, err := model.SelectQuestion(db, id)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		} else if question == nil {
			// If the quiz is nil, then there is none.
			respondWithError(w, http.StatusBadRequest, errorNoSuchRecord(id))
			return
		}

		// Marshal a response.
		response, err := ffjson.Marshal(question.ToDTO())
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Respond with success.
		respondWithSuccess(w, response)
		return
	}
}
