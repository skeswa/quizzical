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
	maxPictureFileSize                  = 2 * 1024 * 1024
	routeVarQuestionID                  = "id"
	questionFormFieldAnswer             = "answer"
	questionFormFieldSource             = "source"
	questionFormFieldSourcePage         = "sourcePage"
	questionFormFieldQuestionID         = "questionId"
	questionFormFieldDifficultyID       = "difficultyId"
	questionFormFieldAnswerPicture      = "answerPicture"
	questionFormFieldMultipleChoice     = "multipleChoice"
	questionFormFieldQuestionPicture    = "answerPicture"
	questionFormFieldRequiresCalculator = "requiresCalculator"
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
		sourceStr := r.FormValue(questionFormFieldSource)
		var source *string
		if len(sourceStr) > 0 {
			source = &sourceStr
		}
		sourcePageStr := r.FormValue(questionFormFieldSourcePage)
		var sourcePage *int
		if len(sourcePageStr) > 0 {
			sourcePageNumber, conversionErr := strconv.Atoi(sourcePageStr)
			if conversionErr != nil {
				respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldSourcePage))
				return
			}

			sourcePage = &sourcePageNumber
		}
		requiresCalculatorStr := r.FormValue(questionFormFieldRequiresCalculator)
		requiresCalculator, err := strconv.ParseBool(requiresCalculatorStr)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldRequiresCalculator))
			return
		}

		// Process the multi-part form.
		r.ParseMultipartForm(maxPictureFileSize)

		// Read the input question picture file from the form.
		inputAnswerPictureFile, inputAnswerPictureFileHeader, err := r.FormFile(questionFormFieldAnswerPicture)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldAnswerPicture))
			return
		}

		// Read the input question picture file from the form.
		inputQuestionPictureFile, inputQuestionPictureFileHeader, err := r.FormFile(questionFormFieldQuestionPicture)
		if err != nil {
			respondWithError(w, http.StatusBadRequest, errorInvalidJSONPayloadField(questionFormFieldQuestionPicture))
			return
		}

		// Close both files after the response is sent.
		defer inputAnswerPictureFile.Close()
		defer inputQuestionPictureFile.Close()

		// Create the output answer picture file.
		inputAnswerPictureFileExt := filepath.Ext(inputAnswerPictureFileHeader.Filename)
		outputAnswerPictureFileName := uuid.NewV4().String() + inputAnswerPictureFileExt
		outputAnswerPictureFile, err := os.Create(filepath.Join(c.ImagesPath, outputAnswerPictureFileName))
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Close both files after the response is sent.
		defer outputAnswerPictureFile.Close()

		// Create the output question picture file.
		inputQuestionPictureFileExt := filepath.Ext(inputQuestionPictureFileHeader.Filename)
		outputQuestionPictureFileName := uuid.NewV4().String() + inputQuestionPictureFileExt
		outputQuestionPictureFile, err := os.Create(filepath.Join(c.ImagesPath, outputQuestionPictureFileName))
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Close both files after the response is sent.
		defer outputQuestionPictureFile.Close()

		// Perform the copy from input to output.
		_, err = io.Copy(outputAnswerPictureFile, inputAnswerPictureFile)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Perform the copy from input to output.
		_, err = io.Copy(outputQuestionPictureFile, inputQuestionPictureFile)
		if err != nil {
			respondWithError(w, http.StatusInternalServerError, err.Error())
			return
		}

		// Interface with the database.
		id, err := model.InsertQuestion(
			db,
			answer,
			source,
			sourcePage,
			questionID,
			difficultyID,
			outputAnswerPictureFileName,
			multipleChoice,
			outputQuestionPictureFileName,
			requiresCalculator,
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
