
import { bindActionCreators } from 'redux'
import React, { Component } from 'react'
import { connect } from 'react-redux'
import classNames from 'classnames'

import style from './style.css'
import actions from 'actions'
import ListError from 'components/ListError'
import ListEmpty from 'components/ListEmpty'
import ListLoader from 'components/ListLoader'
import ListButtons from 'components/ListButtons'
import { pictureNameToBackgroundUrl } from './helpers'

class QuestionList extends Component {
  componentWillMount() {
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

  onRefreshClicked() {
    loadData();
  }

  onCreateClicked() {
    // TODO(skeswa): show the create form.
  }

  render() {
    const { questions, loadingError, isDataLoading } = this.props;

    let content;
    if (loadingError) {
      content = <ListError error={loadingError} />
    } else if (isDataLoading) {
      content = <ListLoader />
    } else if (questions.length < 1) {
      content = <ListEmpty />
    } else {
      content = questions
        .map(question => (
          <QuestionListItem
            key={question.id}
            onClick={::this.onClick}
            question={question} />
        ))
    }

    return (
      <div className={style.main}>
        <div className={style.content}>{content}</div>
        <div className={style.buttons}>
          <ListButtons
            disabled={isDataLoading}
            onCreateClicked={::this.onCreateClicked}
            onRefreshClicked={::this.onRefreshClicked} />
        </div>
      </div>
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

    loadingError: (
      state.question.loadAllError ||
      state.category.loadAllError ||
      state.difficulty.loadAllError
    ),
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
