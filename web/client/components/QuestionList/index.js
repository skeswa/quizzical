
import Dialog from 'material-ui/Dialog'
import classNames from 'classnames'
import FlatButton from 'material-ui/FlatButton'
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import ListError from 'components/ListError'
import ListEmpty from 'components/ListEmpty'
import ListLoader from 'components/ListLoader'
import ListButtons from 'components/ListButtons'
import QuestionListItem from './item'
import QuestionCreationForm from 'components/QuestionCreationForm'

class QuestionList extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    questions:          React.PropTypes.array.isRequired,
    categories:         React.PropTypes.array.isRequired,
    difficulties:       React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

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
      const listItems = questions
        .map(question => (
          <QuestionListItem
            key={question.id}
            onClick={::this.onItemClicked}
            question={question} />
        ))
      const firstColumnItems = listItems.filter((_, i) => i % 2 === 0)
      const secondColumnItems = listItems.filter((_, i) => i % 2 === 1)

      content = (
        <div className={style.list}>
          <div className={style.listColumns}>
            <div className={style.listColumn}>
              {firstColumnItems}
            </div>
            <div className={style.listColumn}>
              {secondColumnItems}
            </div>
          </div>
        </div>
      )
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

export default QuestionList
