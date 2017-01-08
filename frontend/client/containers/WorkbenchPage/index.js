
import { connect } from 'react-redux'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import actions from 'actions'
import QuizResults from 'components/QuizResults'

const SUBMISSION = {
  quiz: {},
  responses: [
    {
      "id": 3,
      "skipped": true,
      "correct": true,
      "response": "A",
      "secondsElapsed": 51,
      "quizProblem": {
        "id": 101919,
        "ordinal": 0,
        "problem": {
          "answer": "A",
          "sourcePageNumber": 5,
          "sourceIndexWithinPage": 12,
          "difficulty": {
            "name": "Medium",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": true
        }
      }
    },
    {
      "id": 10,
      "skipped": false,
      "correct": false,
      "response": "A",
      "secondsElapsed": 27,
      "quizProblem": {
        "id": 1111,
        "ordinal": 1,
        "problem": {
          "answer": "E",
          "sourcePageNumber": 6,
          "sourceIndexWithinPage": 11,
          "difficulty": {
            "name": "Medium",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": false
        }
      }
    },
    {
      "id": 9,
      "skipped": false,
      "correct": false,
      "response": "A",
      "secondsElapsed": 18,
      "quizProblem": {
        "id": 8660,
        "ordinal": 2,
        "problem": {
          "id": 9021,
          "answer": "11/7",
          "sourcePageNumber": 11,
          "sourceIndexWithinPage": 12,
          "difficulty": {
            "name": "Medium",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": false
        }
      }
    },
    {
      "id": 8,
      "skipped": false,
      "correct": true,
      "response": "A",
      "secondsElapsed": 17,
      "quizProblem": {
        "id": 18292,
        "ordinal": 3,
        "problem": {
          "answer": "11/7",
          "sourcePageNumber": 11,
          "sourceIndexWithinPage": 5,
          "difficulty": {
            "name": "Medium",
            "color": "red"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": true
        }
      }
    },
    {
      "id": 5,
      "skipped": true,
      "correct": true,
      "response": "A",
      "secondsElapsed": 73,
      "quizProblem": {
        "id": 7008,
        "ordinal": 4,
        "problem": {
          "answer": "11/7",
          "sourcePageNumber": 12,
          "sourceIndexWithinPage": 5,
          "difficulty": {
            "name": "Easy",
            "color": "red"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": true
        }
      }
    },
    {
      "id": 4,
      "skipped": true,
      "correct": false,
      "response": "A",
      "secondsElapsed": 74,
      "quizProblem": {
        "id": 89202,
        "ordinal": 5,
        "problem": {
          "answer": "D",
          "sourcePageNumber": 9,
          "sourceIndexWithinPage": 12,
          "difficulty": {
            "name": "Hard",
            "color": "red"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": true
        }
      }
    },
    {
      "id": 9,
      "skipped": false,
      "correct": false,
      "response": "A",
      "secondsElapsed": 23,
      "quizProblem": {
        "id": 102838,
        "ordinal": 6,
        "problem": {
          "answer": "11/7",
          "sourcePageNumber": 5,
          "sourceIndexWithinPage": 9,
          "difficulty": {
            "name": "Easy",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": false,
          "requiresCalculator": false
        }
      }
    },
    {
      "id": 9,
      "skipped": true,
      "correct": true,
      "response": "A",
      "secondsElapsed": 14,
      "quizProblem": {
        "id": 286328,
        "ordinal": 7,
        "problem": {
          "answer": "11/7",
          "sourcePageNumber": 12,
          "sourceIndexWithinPage": 5,
          "difficulty": {
            "name": "Easy",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": false,
          "requiresCalculator": false
        }
      }
    },
    {
      "id": 10,
      "skipped": true,
      "correct": true,
      "response": "A",
      "secondsElapsed": 57,
      "quizProblem": {
        "id": 13948,
        "ordinal": 8,
        "problem": {
          "answer": "D",
          "sourcePageNumber": 9,
          "sourceIndexWithinPage": 5,
          "difficulty": {
            "name": "Easy",
            "color": "yellow"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": false,
          "requiresCalculator": true
        }
      }
    },
    {
      "id": 2,
      "skipped": false,
      "correct": false,
      "response": "A",
      "secondsElapsed": 4,
      "quizProblem": {
        "id": 8723,
        "ordinal": 9,
        "problem": {
          "answer": "3/4",
          "sourcePageNumber": 9,
          "sourceIndexWithinPage": 11,
          "difficulty": {
            "name": "Easy",
            "color": "green"
          },
          "answerPicture": {
            "id": 456
          },
          "questionPicture": {
            "id": 455
          },
          "multipleChoice": true,
          "requiresCalculator": false
        }
      }
    }
  ]
}

const WorkbenchPage = (props, context) => {
  return (
    <div style={{
      width: '100%',
      height: '100%',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#ddd',
    }}>
      <div style={{
        display: 'flex',
        width: '420px',
        height: '420px',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.4)',
        borderRadius: '4px',
      }}>
        <QuizResults results={SUBMISSION} />
      </div>
    </div>
  )
}

const reduxify = connect(
  (state, props) => ({}),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      /* bindActionCreators(actions.source, dispatch) */)
  })
)

export default reduxify(WorkbenchPage)
