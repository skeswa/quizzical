package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/skeswa/gauntlet/api/common"
)

func main() {
	// Get the environment configuration or die trying.
	config := common.GetConfig()
	db, err := common.StartDBConnection(config)
	if err != nil {
		log.Fatalln("Failed to connect to the database:", err)
	} else {
		log.Println("Configuration:\n\n" + config.String() + "\n")
	}

	// Close the database connection after exit.
	defer db.Close()

	// Register all of the routes.
	r := mux.NewRouter()

	// Pictures.
	r.HandleFunc("/api/pictures/{name}", getPictureHandler(config)).Methods("GET")

	// Categories.
	r.HandleFunc("/api/categories", getCategoriesHandler(db)).Methods("GET")
	r.HandleFunc("/api/categories", createCategoryHandler(db)).Methods("POST")
	r.HandleFunc("/api/categories/{id:[0-9]+}", getCategoryHandler(db)).Methods("GET")
	r.HandleFunc("/api/categories/{id:[0-9]+}", deleteCategoryHandler(db)).Methods("DELETE")

	// Difficulties.
	r.HandleFunc("/api/difficulties", getDifficultiesHandler(db)).Methods("GET")
	r.HandleFunc("/api/difficulties", createDifficultyHandler(db)).Methods("POST")
	r.HandleFunc("/api/difficulties/{id:[0-9]+}", getDifficultyHandler(db)).Methods("GET")
	r.HandleFunc("/api/difficulties/{id:[0-9]+}", deleteDifficultyHandler(db)).Methods("DELETE")

	// Questions.
	r.HandleFunc("/api/questions", getQuestionsHandler(db)).Methods("GET")
	r.HandleFunc("/api/questions", createQuestionHandler(db, config)).Methods("POST")
	r.HandleFunc("/api/questions/{id:[0-9]+}", getQuestionHandler(db)).Methods("GET")
	r.HandleFunc("/api/questions/{id:[0-9]+}", deleteQuestionHandler(db)).Methods("DELETE")

	// Quizzes.
	r.HandleFunc("/api/quizzes", getQuizzesHandler(db)).Methods("GET")
	r.HandleFunc("/api/quizzes", createQuizHandler(db)).Methods("POST")
	r.HandleFunc("/api/quizzes/{id:[0-9]+}", getQuizHandler(db)).Methods("GET")
	r.HandleFunc("/api/quizzes/{id:[0-9]+}", deleteQuizHandler(db)).Methods("DELETE")

	// Takes.
	r.HandleFunc("/api/takes", getTakesHandler(db)).Methods("GET")
	r.HandleFunc("/api/takes", createTakeHandler(db)).Methods("POST")
	r.HandleFunc("/api/takes/{id:[0-9]+}", getTakeHandler(db)).Methods("GET")
	r.HandleFunc("/api/takes/{id:[0-9]+}", deleteTakeHandler(db)).Methods("DELETE")

	// Start serving.
	log.Printf("Servicing HTTP requests on port %d.\n", config.Port)
	http.ListenAndServe(fmt.Sprintf(":%d", config.Port), r)
}
