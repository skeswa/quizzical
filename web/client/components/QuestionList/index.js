
import { bindActionCreators } from 'redux'
import React, { Component } from 'react'
import RaisedButton from 'material-ui/RaisedButton'
import { connect } from 'react-redux'
import FlatButton from 'material-ui/FlatButton'
import classNames from 'classnames'
import Dialog from 'material-ui/Dialog'

import style from './style.css'
import actions from 'actions'
import ListError from 'components/ListError'
import ListEmpty from 'components/ListEmpty'
import ListLoader from 'components/ListLoader'
import ListButtons from 'components/ListButtons'
import QuestionCreationForm from 'components/QuestionCreationForm'

class QuestionList extends Component {
  state = {
    creationDialogVisible: false,
  }

  componentWillMount() {
    // Attempt to load questions.
    if (this.props.dataShouldBeLoaded) {
      this.props.actions.loadQuestions()
      this.props.actions.loadCategories()
      this.props.actions.loadDifficulties()
    }
  }

  onRefreshListClicked() {
    // Re-load everything.
    this.props.actions.loadQuestions()
    this.props.actions.loadCategories()
    this.props.actions.loadDifficulties()
  }

  onOpenCreateClicked() {
    // TODO(skeswa): show the create form.
    this.setState({ creationDialogVisible: true })
  }

  onItemClicked() {
    // TODO(skeswa): activate any selection events that may exist.
  }

  onCreateQuestionClicked() {
    // TODO(skeswa): implement this.
  }

  onCancelQuestionClicked() {
    this.setState({ creationDialogVisible: false })
  }

  render() {
    const { creationDialogVisible } = this.state
    const { questions, loadingError, isDataLoading } = this.props

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
            onClick={::this.onItemClicked}
            question={question} />
        ))
    }

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onTouchTap={::this.onCancelQuestionClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        onTouchTap={::this.onCreateQuestionClicked} />
    ]

    return (
      <div className={style.main}>
        <div className={style.content}>{content}</div>
        <div className={style.buttons}>
          <ListButtons
            disabled={isDataLoading}
            onCreateClicked={::this.onOpenCreateClicked}
            onRefreshClicked={::this.onRefreshListClicked} />
        </div>

        <Dialog
          title="Create New Question"
          actions={dialogActions}
          modal={true}
          open={creationDialogVisible}
          onRequestClose={::this.onCancelQuestionClicked}
          autoScrollBodyContent={true}>
          <QuestionCreationForm />
        </Dialog>
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

function pictureNameToBackgroundUrl(pictureName) {
  return `/api/pictures/${pictureName}`
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
