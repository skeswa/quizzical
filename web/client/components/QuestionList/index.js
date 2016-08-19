
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
    loadingError:                   null,
    isDataLoading:                  false,
    questionCreationError:          null,
    questionCreationInProgress:     false,
    questionCreationDialogVisible:  false,
  }

  componentWillMount() {
    // Attempt to load questions.
    if (this.props.dataShouldBeLoaded) {
      this.loadData()
    }
  }

  loadData() {
    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        this.props.actions.loadQuestions(),
        this.props.actions.loadCategories(),
        this.props.actions.loadDifficulties(),
      ])
      .then(() => this.setState({ isDataLoading: false }))
      .catch(err => this.setState({ loadingError: err }))
  }

  onRefreshListClicked() {
    this.loadData()
  }

  onOpenCreateClicked() {
    this.setState({
      questionCreationError: null,
      questionCreationDialogVisible: true,
    })
  }

  onItemClicked() {
    // TODO(skeswa): activate any selection events that may exist.
  }

  onCreateQuestionClicked() {
    this.setState({
      questionCreationError:      null,
      questionCreationInProgress: true,
    })

    const formData = this.refs.creationForm.getFormData()
    this.props.actions.createQuestion(formData)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            questionCreationError:      resultingAction.payload,
            questionCreationInProgress: false,
          })
        } else {
          this.setState({
            questionCreationInProgress:     false,
            questionCreationDialogVisible:  false,
          })
        }
      })
  }

  onCancelQuestionClicked() {
    this.setState({ questionCreationDialogVisible: false })
  }

  renderCreationDialog() {
    const { actions, categories, difficulties } = this.props
    const {
      isDataLoading,
      questionCreationError,
      questionCreationInProgress,
      questionCreationDialogVisible,
    } = this.state

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        disabled={isDataLoading}
        onTouchTap={::this.onCancelQuestionClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        disabled={isDataLoading}
        onTouchTap={::this.onCreateQuestionClicked} />
    ]

    return (
      <Dialog
        title="Create New Question"
        actions={dialogActions}
        modal={true}
        open={questionCreationDialogVisible}
        onRequestClose={::this.onCancelQuestionClicked}
        autoScrollBodyContent={true}>
        <QuestionCreationForm
          ref="creationForm"
          error={questionCreationError}
          loading={questionCreationInProgress}
          categories={categories}
          difficulties={difficulties}
          createCategory={actions.createCategory}
          createDifficulty={actions.createDifficulty} />
      </Dialog>
    )
  }

  render() {
    const { questions } = this.props
    const { isDataLoading, loadingError } = this.state

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

    return (
      <div className={style.main}>
        <div className={style.content}>{content}</div>
        <div className={style.buttons}>
          <ListButtons
            disabled={isDataLoading}
            onCreateClicked={::this.onOpenCreateClicked}
            onRefreshClicked={::this.onRefreshListClicked} />
        </div>

        {this.renderCreationDialog()}
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
    questions:          state.question.list,
    categories:         state.category.list,
    difficulties:       state.difficulty.list,
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
