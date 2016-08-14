package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/skeswa/gauntlet/api/common"
	"github.com/skeswa/gauntlet/api/handler"
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
	r.HandleFunc("/api/pictures/{name}", handler.GetPicture(config)).Methods("GET")

	// Categories.
	r.HandleFunc("/api/categories", handler.GetCategories(db)).Methods("GET")
	r.HandleFunc("/api/categories", handler.CreateCategory(db)).Methods("POST")
	r.HandleFunc("/api/categories/{id:[0-9]+}", handler.GetCategory(db)).Methods("GET")
	r.HandleFunc("/api/categories/{id:[0-9]+}", handler.DeleteCategory(db)).Methods("DELETE")

	// Difficulties.
	r.HandleFunc("/api/difficulties", handler.GetDifficulties(db)).Methods("GET")
	r.HandleFunc("/api/difficulties", handler.CreateDifficulty(db)).Methods("POST")
	r.HandleFunc("/api/difficulties/{id:[0-9]+}", handler.GetDifficulty(db)).Methods("GET")
	r.HandleFunc("/api/difficulties/{id:[0-9]+}", handler.DeleteDifficulty(db)).Methods("DELETE")

	// Questions.
	r.HandleFunc("/api/questions", handler.GetQuestions(db)).Methods("GET")
	r.HandleFunc("/api/questions", handler.CreateQuestion(db, config)).Methods("POST")
	r.HandleFunc("/api/questions/{id:[0-9]+}", handler.GetQuestion(db)).Methods("GET")
	r.HandleFunc("/api/questions/{id:[0-9]+}", handler.DeleteQuestion(db)).Methods("DELETE")

	// Quizzes.
	r.HandleFunc("/api/quizzes", handler.GetQuizzes(db)).Methods("GET")
	r.HandleFunc("/api/quizzes", handler.CreateQuiz(db)).Methods("POST")
	r.HandleFunc("/api/quizzes/{id:[0-9]+}", handler.GetQuiz(db)).Methods("GET")
	r.HandleFunc("/api/quizzes/{id:[0-9]+}", handler.DeleteQuiz(db)).Methods("DELETE")

	// Takes.
	r.HandleFunc("/api/takes", handler.GetTakes(db)).Methods("GET")
	r.HandleFunc("/api/takes", handler.CreateTake(db)).Methods("POST")
	r.HandleFunc("/api/takes/{id:[0-9]+}", handler.GetTake(db)).Methods("GET")
	r.HandleFunc("/api/takes/{id:[0-9]+}", handler.DeleteTake(db)).Methods("DELETE")

	// Start serving.
	log.Printf("Servicing HTTP requests on port %d.\n", config.Port)
	http.ListenAndServe(fmt.Sprintf(":%d", config.Port), r)
}
