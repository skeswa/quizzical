
import React, { Component, PropTypes } from 'react'

import StudentLessonsList from 'components/StudentLessonsList'

const fakeData = {
  currentLesson: { id: 1001, chapter: 3, section: 2, started: true, name: 'System of Linear Equations' },
  upcomingLessons: [
    { id: 1002, chapter: 3, section: 3, started: false, name: 'Equations with One Variable' },
    { id: 1003, chapter: 3, section: 4, started: false, name: 'Polynomials' },
    { id: 1004, chapter: 3, section: 5, started: false, name: 'Another One' },
    { id: 1005, chapter: 3, section: 6, started: false, name: 'Another One Pt. 2' },
  ],
}

class StudentLessonsPage extends Component {
  render() {
    return (
      <StudentLessonsList
        currentLesson={fakeData.currentLesson}
        upcomingLessons={fakeData.upcomingLessons} />
    )
  }
}

export default StudentLessonsPage