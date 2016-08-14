
import { bindActionCreators } from 'redux'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import classNames from 'classnames'

import style from './style.css'
import actions from 'actions'
import { pictureNameToBackgroundUrl } from './helpers'

class QuestionList extends Component {
  componentWillMount() {
    const props = this.props

    // Load data pre-emptively.
    this.loadData()
  }

  // Gets data needed to render this component form the database.
  loadData() {
    const props = this.props

    // Attempt to load questions.
    if (props.dataShouldBeLoaded) {
      props.actions.loadQuestions()
      props.actions.loadCategories()
      props.actions.loadDifficulties()
    }
  }

  render() {
    console.log('props', this.props)

    const listItems = this.props.questions
      .map(question => (
        <QuestionListItem
          key={question.id}
          question={question}
          onClick={::this.onClick} />
      ))

    return (
      <div className={style.main}>{listItems}</div>
    )
  }
}

const QuestionListItem = (props, context) => {
  const {
    onClick,
    question: {
      id: questionId,
      picture: questionPicture,
      category: { name: questionCategoryName },
      difficulty: {
        name: questionDifficultyName,
        color: questionDifficultyColor,
      },
      dateCreated: questionDateCreated,
      multipleChoice: isQuestionMultipleChoice,
    }
  } = props

  const questionBackgroundUrl = pictureNameToBackgroundUrl(questionPicture)
  const questionMultipleChoice = isQuestionMultipleChoice ? 'multiple choice' : 'full answer'

  return (
    <div className={style.listItem} onClick={onClick}>
      <div
        className={style.listItemPicture}
        style={{ backgroundUrl: questionBackgroundUrl }} />
      <div className={style.listItemInfo}>
        <div className={style.listItemCategory}>{questionCategoryName}</div>
        <div
          className={style.listItemDifficulty}
          style={{ backgroundColor: questionDifficultyColor }}>{questionDifficultyName}</div>
        <div className={style.listItemMultipleChoice}>{questionMultipleChoice}</div>
        <div className={style.listItemDateCreated}>{questionDateCreated}</div>
      </div>
    </div>
  )
}

const reduxify = connect(
  (state, props) => ({
    questions: state.question.list,
    categories: state.category.list,
    difficulties: state.difficulty.list,

    isDataLoading: (
      state.question.pendingRequests > 0 ||
      state.category.pendingRequests > 0 ||
      state.difficulty.pendingRequests > 0
    ),
    dataShouldBeLoaded: (
      !state.question.loaded ||
      !state.category.loaded ||
      !state.difficulty.loaded
    ),
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.question, dispatch),
      bindActionCreators(actions.category, dispatch),
      bindActionCreators(actions.difficulty, dispatch),
    )
  })
)

export default reduxify(QuestionList)
