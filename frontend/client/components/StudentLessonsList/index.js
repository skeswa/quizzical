
import React, { Component, PropTypes } from 'react'

import StudentLessonCard from './card.js'
import style from './index.css'

class StudentLessonsList extends Component {
  static propTypes = {
    currentLesson: PropTypes.object.isRequired,
    upcomingLessons: PropTypes.array.isRequired,
  }

  onLessonStartRequested(lessonId) {
    console.log(`onLessonStartRequested('${lessonId}')`)
  }

  onLessonQuizStartRequested(lessonId) {
    console.log(`onLessonQuizStartRequested('${lessonId}')`)
  }

  render() {
    const { currentLesson, upcomingLessons } = this.props

    return (
      <div className={style.main}>
        <div className={style.section}>
          <div className={style.heading}>Current Lessons</div>
          <div className={style.cards}>
            <StudentLessonCard
                lesson={currentLesson}
                onLessonStartRequested={this.onLessonStartRequested}
                onLessonQuizStartRequested={this.onLessonQuizStartRequested} />
          </div>
        </div>
        <div className={style.section}>
          <div className={style.heading}>Upcoming Lessons</div>
          <div className={style.cards}>
          {
            upcomingLessons.map(lesson => (
              <StudentLessonCard
                  key={lesson.id}
                  lesson={lesson}
                  onLessonStartRequested={this.onLessonStartRequested}
                  onLessonQuizStartRequested={this.onLessonQuizStartRequested} />
            ))
          }
          </div>
        </div>
      </div>
    )
  }
}

export default StudentLessonsList