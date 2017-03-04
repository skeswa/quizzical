#!/bin/sh
export KETTLE_ROOT="/Applications/devtools/pentaho-pdi-ce-7/data-integration"
export KETTLE_HOME="/Users/mduduzikeswa/Documents/dev/mazasoft/projects/quizical/quizzical/bi/pdi/kettle_home_local"
cd $KETTLE_ROOT
./spoon.sh $*
