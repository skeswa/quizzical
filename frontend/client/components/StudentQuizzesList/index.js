
import React, { Component, PropTypes } from 'react'
import autobind from 'autobind-decorator'
import StudentQuizSubmissionCard from './quiz-submission-card.js'
import StudentQuizCard from './quiz-card.js'
import style from './index.css'

class StudentQuizzesList extends Component {
  static propTypes = {
    latestQuiz:   PropTypes.object.isRequired,
    recentQuizzes: PropTypes.array.isRequired,
    unsubmittedQuizzes: PropTypes.array.isRequired,
  }

  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  }

  @autobind
  onQuizSubmissionReview(quizId) {
    this.context.router.history.push(`/quiz/${quizId}/results`)
    console.log(`onQuizSubmissionReview('${quizId}')`)
  }


  render() {
    const { latestQuiz, recentQuizzes, unsubmittedQuizzes } = this.props

    return (
      <div className={style.main}>
         {latestQuiz.id ?
           <div className={style.section}>
           <div className={style.heading}>Latest Quiz</div>
            <div className={style.cards}>
              <StudentQuizSubmissionCard
                  key={latestQuiz.id}
                  submission={latestQuiz}
                  onQuizSubmissionReview={this.onQuizSubmissionReview} />
            </div>
            </div> :
            <div className={style.heading}>No Latest Quiz</div>
        }

        <div className={style.section}>
          <div className={style.heading}>Recent Quizzes</div>
          <div className={style.cards}>
          {
            recentQuizzes.map(submission => (
              <StudentQuizSubmissionCard
                  key={submission.id}
                  submission={submission}
                  onQuizSubmissionReview={this.onQuizSubmissionReview} />
            ))
          }
          </div>
        </div>
        <div className={style.section}>
          <div className={style.heading}>Unsubmitted Quizzes</div>
          <div className={style.cards}>
          {
            unsubmittedQuizzes.map(quiz => (
              <StudentQuizCard
                  key={quiz.id}
                  quiz={quiz}
                  onQuizCancelRequested={this.onQuizCancelRequested}
                  onQuizStartRequested={this.onQuizStartRequested} />
            ))
          }
          </div>
        </div>
      </div>
    )
  }
}

export default StudentQuizzesList
