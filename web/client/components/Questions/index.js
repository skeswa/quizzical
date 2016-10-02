
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
import QuestionGrid from 'components/QuestionGrid'
import QuestionTable from 'components/QuestionTable'
import QuestionCreationForm from 'components/QuestionCreationForm'

class Questions extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    sources:            React.PropTypes.array.isRequired,
    questions:          React.PropTypes.array.isRequired,
    categories:         React.PropTypes.array.isRequired,
    difficulties:       React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

  state = {
    gridVisible:                    true,
    loadingError:                   null,
    isDataLoading:                  false,
    categoryFilterId:               null,
    difficultyFilterId:             null,
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
    const {
      loadSources,
      loadQuestions,
      loadCategories,
      loadDifficulties,
    } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        loadSources(),
        loadQuestions(),
        loadCategories(),
        loadDifficulties(),
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
      questionCreationError: null,
      questionCreationDialogVisible: true,
    })
  }

  onItemClicked() {
    // TODO(skeswa): activate any selection events that may exist.
  }

  onSwitchToGridClicked() {
    this.setState({ gridVisible: true })
  }

  onSwitchToTableClicked() {
    this.setState({ gridVisible: false })
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

  onCategoryFilterIdChanged(e, i, value) {
    this.setState({ categoryFilterId: value })
  }

  onDifficultyFilterIdChanged(e, i, value) {
    this.setState({ difficultyFilterId: value })
  }

  renderCreationDialog() {
    const {
      actions,
      sources,
      categories,
      difficulties,
    } = this.props
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
        open={questionCreationDialogVisible}
        onRequestClose={::this.onCancelQuestionClicked}
        autoScrollBodyContent={true}>
        <QuestionCreationForm
          ref="creationForm"
          error={questionCreationError}
          loading={questionCreationInProgress}
          sources={sources}
          categories={categories}
          difficulties={difficulties}
          createSource={actions.createSource}
          createCategory={actions.createCategory}
          createDifficulty={actions.createDifficulty} />
      </Dialog>
    )
  }

  render() {
    const { questions, categories, difficulties } = this.props
    const {
      gridVisible,
      loadingError,
      isDataLoading,
      categoryFilterId,
      difficultyFilterId,
    } = this.state

    const filteredQuestions = questions
      .filter(question => {
        if (categoryFilterId !== null) {
          return question.category.id === categoryFilterId
        }

        return true
      })
      .filter(question => {
        if (difficultyFilterId !== null) {
          return question.difficulty.id === difficultyFilterId
        }

        return true
      })

    let content = null
    if (loadingError) {
      content = <ListError error={loadingError} />
    } else if (isDataLoading) {
      content = <ListLoader />
    } else if (filteredQuestions.length < 1) {
      content = <ListEmpty />
    } else if (gridVisible) {
      content = <QuestionGrid questions={filteredQuestions} />
    } else {
      content = <QuestionTable questions={filteredQuestions} />
    }

    const categoryMenuItems = categories
      .map(category => (
        <MenuItem
          key={category.id}
          value={category.id}
          primaryText={category.name} />
      ))
    const difficultyMenuItems = difficulties
      .map(difficulty => (
        <MenuItem
          key={difficulty.id}
          value={difficulty.id}
          primaryText={difficulty.name} />
      ))
    const allFilterMenuItem = (
      <MenuItem
        key="all"
        value={null}
        primaryText="All" />
    )

    categoryMenuItems.unshift(
      <MenuItem
        key="all"
        value={null}
        primaryText="All Categories" />
    )
    difficultyMenuItems.unshift(
      <MenuItem
        key="all"
        value={null}
        primaryText="All Difficulties" />
    )

    return (
      <div className={style.main}>
        <div className={style.filterBarWrapper}>
          <div className={classNames(style.filterBar, { [style.filterBar__wide]: !gridVisible })}>
            <FontIcon
              className="material-icons"
              color="#ffffff">filter_list</FontIcon>
            <SelectField
              value={categoryFilterId}
              onChange={::this.onCategoryFilterIdChanged}
              className={style.filterSelect}
              labelStyle={{ color: '#fff' }}>
              {categoryMenuItems}
            </SelectField>
            <SelectField
              value={difficultyFilterId}
              onChange={::this.onDifficultyFilterIdChanged}
              className={style.filterSelect}
              labelStyle={{ color: '#fff' }}>
              {difficultyMenuItems}
            </SelectField>
          </div>
        </div>
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

export default Questions
