
import Dialog from 'material-ui/Dialog'
import FontIcon from 'material-ui/FontIcon'
import MenuItem from 'material-ui/MenuItem'
import classNames from 'classnames'
import FlatButton from 'material-ui/FlatButton'
import SelectField from 'material-ui/SelectField'
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
    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        this.props.actions.loadQuestions(),
        this.props.actions.loadCategories(),
        this.props.actions.loadDifficulties(),
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
    const { questions, categories, difficulties } = this.props
    const {
      isDataLoading,
      loadingError,
      categoryFilterId,
      difficultyFilterId,
    } = this.state

    let content = null
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
        .filter(question => {
          if (categoryFilterId !== null) {
            return question.category.id === categoryFilterId
          }

          if (difficultyFilterId !== null) {
            return question.difficulty.id === difficultyFilterId
          }
        })

      content = (
        <div className={style.list}>
          <div className={style.listItems}>
            {listItems}
          </div>
        </div>
      )
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
          <div className={style.filterBar}>
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
            onCreateClicked={::this.onOpenCreateClicked}
            onRefreshClicked={::this.onRefreshListClicked} />
        </div>

        {this.renderCreationDialog()}
      </div>
    )
  }
}

export default QuestionList
