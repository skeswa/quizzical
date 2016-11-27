
import Dialog from 'material-ui/Dialog'
import FontIcon from 'material-ui/FontIcon'
import MenuItem from 'material-ui/MenuItem'
import classNames from 'classnames'
import FlatButton from 'material-ui/FlatButton'
import SelectField from 'material-ui/SelectField'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'

import style from './style.css'
import ListError from 'components/ListError'
import ListEmpty from 'components/ListEmpty'
import ListLoader from 'components/ListLoader'
import ListButtons from 'components/ListButtons'
import QuizAttemptGrid from 'components/QuizAttemptGrid'
import QuizGenerationForm from 'components/QuizGenerationForm'

class QuizAttempts extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    categories:         React.PropTypes.array.isRequired,
    quizAttempts:       React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

  state = {
    gridVisible:                  true,
    loadingError:                 null,
    isDataLoading:                false,
    quizGenerationError:          null,
    quizGenerationInProgress:     false,
    quizGenerationDialogVisible:  false,
  }

  componentWillMount() {
    // QuizAttempt to load attempts.
    if (this.props.dataShouldBeLoaded) {
      this.loadData()
    }
  }

  loadData() {
    const { loadCategories, loadQuizAttempts } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        loadCategories(),
        loadQuizAttempts(),
      ])
      .then(resultingActions => {
        let error = null
        for (let i = 0; i < resultingActions.length; i++) {
          if (resultingActions[i].error) {
            error = resultingActions[i].payload
            break
          }
        }

        if (error) {
          this.setState({
            loadingError:   error,
            isDataLoading:  false,
          })
        } else {
          this.setState({ isDataLoading: false })
        }
      })
  }

  onRefreshListClicked() {
    this.loadData()
  }

  onOpenCreateClicked() {
    this.setState({
      quizGenerationError: null,
      quizGenerationDialogVisible: true,
    })
  }

  onItemClicked() {
    // TODO(skeswa): activate any selection events that may exist.
  }

  onSwitchToGridClicked() {
    this.setState({ gridVisible: true })
  }

  onSwitchToTableClicked() {
    // TODO(skeswa): enable this.
    // this.setState({ gridVisible: false })
  }

  onCreateQuizAttemptClicked() {
    this.setState({
      quizGenerationError:      null,
      quizGenerationInProgress: true,
    })

    const formData = this.refs.generationForm.getJSON()
    this.props.actions.generateQuiz(formData)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            quizGenerationError:      resultingAction.payload,
            quizGenerationInProgress: false,
          })
        } else {
          this.setState({
            quizGenerationInProgress:     false,
            quizGenerationDialogVisible:  false,
          })
        }
      })
  }

  onCancelQuizAttemptClicked() {
    this.setState({ quizGenerationDialogVisible: false })
  }

  renderCreationDialog() {
    const { actions, categories } = this.props
    const {
      isDataLoading,
      quizGenerationError,
      quizGenerationInProgress,
      quizGenerationDialogVisible,
    } = this.state

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        disabled={isDataLoading}
        onTouchTap={::this.onCancelQuizAttemptClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        disabled={isDataLoading}
        onTouchTap={::this.onCreateQuizAttemptClicked} />
    ]

    return (
      <Dialog
        title="Describe Your Quiz"
        actions={dialogActions}
        open={quizGenerationDialogVisible}
        onRequestClose={::this.onCancelQuizAttemptClicked}
        autoScrollBodyContent={true}>
        <QuizGenerationForm
          ref="generationForm"
          error={quizGenerationError}
          loading={quizGenerationInProgress}
          categories={categories}
          createCategory={actions.createCategory} />
      </Dialog>
    )
  }

  render() {
    const { quizAttempts } = this.props
    const {
      gridVisible,
      loadingError,
      isDataLoading,
      categoryFilterId,
      difficultyFilterId,
    } = this.state

    let content = null
    if (loadingError) {
      content = <ListError error={loadingError} />
    } else if (isDataLoading) {
      content = <ListLoader />
    } else if (quizAttempts.length < 1) {
      content = <ListEmpty />
    } else if (gridVisible) {
      content = <QuizAttemptGrid quizAttempts={quizAttempts} />
    } else {
      // TODO(skeswa): fix
      // content = <QuizAttemptTable attempts={filteredQuizAttempts} />
    }

    return (
      <div className={style.main}>
        <div className={style.content}>{content}</div>
        <div className={style.buttons}>
          <ListButtons
            disabled={isDataLoading}
            switchToGrid={!gridVisible}
            onCreateClicked={::this.onOpenCreateClicked}
            onRefreshClicked={::this.onRefreshListClicked}
            onSwitchToGridClicked={::this.onSwitchToGridClicked}
            onSwitchToTableClicked={::this.onSwitchToTableClicked} />
        </div>

        {this.renderCreationDialog()}
      </div>
    )
  }
}

export default QuizAttempts
