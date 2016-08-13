package main

import (
	"net/http"
	"path/filepath"

	"github.com/gorilla/mux"
	"github.com/skeswa/gauntlet/api/common"
)

const (
	routeVarPictureName = "name"
)

func getPictureHandler(c *common.Config) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the category id.
		name := mux.Vars(r)[routeVarPictureName]
		if len(name) < 1 {
			respondWithError(w, http.StatusBadRequest, errorInvalidRouteVar(routeVarPictureName))
			return
		}

		// Serve this picture from the images folder.
		http.ServeFile(w, r, filepath.Join(c.ImagesPath, name))
	}
}
