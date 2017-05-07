
import React, { Component, PropTypes } from 'react'

import StudentLessonsList from 'components/StudentLessonsList'

const fakeData = {
  currentLesson: { id: 1001, chapter: 3, section: 2, name: 'System of Linear Equations' },
  upcomingLessons: [
    { id: 1002, chapter: 3, section: 3, name: 'Equations with One Variable' },
    { id: 1003, chapter: 3, section: 4, name: 'Polynomials' },
    { id: 1004, chapter: 3, section: 5, name: 'Another One' },
    { id: 1005, chapter: 3, section: 6, name: 'Another One Pt. 2' },
  ],
}

class StudentLessonsPage extends Component {
  render() {
    <StudentLessonsList
        currentLesson={fakeData.currentLesson}
        upcomingLessons={fakeData.upcomingLessons} />
  }
}

export default StudentLessonsPage