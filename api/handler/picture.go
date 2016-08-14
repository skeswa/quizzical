package handler

import (
	"net/http"
	"path/filepath"

	"github.com/gorilla/mux"
	"github.com/skeswa/gauntlet/api/common"
	"github.com/skeswa/gauntlet/api/handler/helpers"
)

const (
	routeVarPictureName = "name"
)

func GetPicture(c *common.Config) func(http.ResponseWriter, *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		// Read the category id.
		name := mux.Vars(r)[routeVarPictureName]
		if len(name) < 1 {
			helpers.RespondWithError(w, http.StatusBadRequest, helpers.ErrorInvalidRouteVar(routeVarPictureName))
			return
		}

		// Serve this picture from the images folder.
		http.ServeFile(w, r, filepath.Join(c.ImagesPath, name))
	}
}
