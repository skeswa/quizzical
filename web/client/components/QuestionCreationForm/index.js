
import Dialog from 'material-ui/Dialog'
import ReactDOM from 'react-dom'
import Checkbox from 'material-ui/Checkbox'
import MenuItem from 'material-ui/MenuItem'
import TextField from 'material-ui/TextField'
import FlatButton from 'material-ui/FlatButton'
import SelectField from 'material-ui/SelectField'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton'

import style from './style.css'
import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'
import CategoryCreationForm from 'components/CategoryCreationForm'
import DifficultyCreationForm from 'components/DifficultyCreationForm'

const questionTypeNumericAnswer = 'numericAnswer'
const questionTypeMultipleChoice  = 'multipleChoice'

class QuestionCreationForm extends Component {
  static propTypes = {
    error:                    React.PropTypes.any,
    loading:                  React.PropTypes.bool.isRequired,
    categories:               React.PropTypes.array.isRequired,
    difficulties:             React.PropTypes.array.isRequired,
    createCategory:           React.PropTypes.func.isRequired,
    createDifficulty:         React.PropTypes.func.isRequired,
  }

  state = {
    requiresCalculator:               false,
    selectedCategoryId:               null,
    selectedDifficultyId:             null,
    selectedQuestionType:             null,
    categoryCreationError:            null,
    difficultyCreationError:          null,
    categoryCreationInProgress:       false,
    difficultyCreationInProgress:     false,
    categoryCreationDialogVisible:    false,
    difficultyCreationDialogVisible:  false,
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.error !== nextProps.error) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }
  }

  onCategoryChanged(e, i, value) {
    this.setState({ selectedCategoryId: value })
  }

  onDifficultyChanged(e, i, value) {
    this.setState({ selectedDifficultyId: value })
  }

  onQuestionTypeChanged(e, i, value) {
    this.setState({ selectedQuestionType: value })
  }

  onRequiresCalculatorChecked(e, checked) {
    this.setState({ requiresCalculator: checked })
  }

  onCreateCategoryClicked() {
    this.setState({
      categoryCreationError:      null,
      categoryCreationInProgress: true,
    })

    const json = this.refs.categoryCreationForm.getJSON()
    this.props.createCategory(json)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            categoryCreationError:      resultingAction.payload,
            categoryCreationInProgress: false,
          })
        } else {
          this.setState({
            categoryCreationInProgress:     false,
            categoryCreationDialogVisible:  false,
          })
        }
      })
  }

  onCreateDifficultyClicked() {
    this.setState({
      difficultyCreationError:      null,
      difficultyCreationInProgress: true,
    })

    const json = this.refs.difficultyCreationForm.getJSON()
    this.props.createDifficulty(json)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            difficultyCreationError:      resultingAction.payload,
            difficultyCreationInProgress: false,
          })
        } else {
          this.setState({
            difficultyCreationInProgress:     false,
            difficultyCreationDialogVisible:  false,
          })
        }
      })
  }

  onCancelCategoryClicked() {
    this.setState({ categoryCreationDialogVisible: false })
  }

  onCancelDifficultyClicked() {
    this.setState({ difficultyCreationDialogVisible: false })
  }

  onOpenDifficultyCreationDialogClicked() {
    this.setState({
      difficultyCreationError:          null,
      difficultyCreationDialogVisible:  true,
    })
  }

  onOpenCategoryCreationDialogClicked() {
    this.setState({
      categoryCreationError:          null,
      categoryCreationDialogVisible:  true,
    })
  }

  getFormData = () => {
    return new FormData(this.refs.form)
  }

  scrollToTop() {
    const node = ReactDOM.findDOMNode(this)
    const parentNode = node.parentNode
    parentNode.scrollTop = 0
  }

  renderHiddenFields() {
    const {
      requiresCalculator,
      selectedCategoryId,
      selectedDifficultyId,
      selectedQuestionType,
    } = this.state

    const multipleChoice = selectedQuestionType
      ? selectedQuestionType === questionTypeMultipleChoice
      : null

    return [
      selectedCategoryId
        ? <input
            key="categoryId"
            type="hidden"
            name="categoryId"
            value={selectedCategoryId} />
        : null,
      selectedDifficultyId
        ? <input
            key="difficultyId"
            type="hidden"
            name="difficultyId"
            value={selectedDifficultyId} />
        : null,
      multipleChoice !== null
        ? <input
            key="multipleChoice"
            type="hidden"
            name="multipleChoice"
            value={multipleChoice} />
        : null,
      <input
        key="requiresCalculator"
        type="hidden"
        name="requiresCalculator"
        value={requiresCalculator} />,
    ]
  }

  renderAnswerField() {
    const { selectedQuestionType } = this.state

    switch (selectedQuestionType) {
      case questionTypeNumericAnswer:
        return (
          <TextField
            name="answer"
            hintText="e.g. 123.4 or 1/2"
            fullWidth={true}
            floatingLabelText="Numeric Answer" />
        )
      case questionTypeMultipleChoice:
        return (
          <div>
            <div className={style.label}>Multiple Choice Answer</div>
            <RadioButtonGroup name="answer" defaultSelected="a">
              <RadioButton value="a" label="A" />
              <RadioButton value="b" label="B" />
              <RadioButton value="c" label="C" />
              <RadioButton value="d" label="D" />
              <RadioButton value="e" label="E" />
            </RadioButtonGroup>
          </div>
        )
    }

    return null;
  }

  renderError() {
    const { error } = this.props

    if (error) {
      const message = error.error ? error.error : error

      return (
        <FormError
          title="Failed to create question"
          message={message} />
      )
    }

    return null
  }

  renderCategoryCreationDialog() {
    const {
      categoryCreationError,
      categoryCreationInProgress,
      categoryCreationDialogVisible,
    } = this.state

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        disabled={categoryCreationInProgress}
        onTouchTap={::this.onCancelCategoryClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        disabled={categoryCreationInProgress}
        onTouchTap={::this.onCreateCategoryClicked} />
    ]

    return (
      <Dialog
        title="Create New Category"
        actions={dialogActions}
        modal={true}
        open={categoryCreationDialogVisible}
        onRequestClose={::this.onCancelCategoryClicked}
        autoScrollBodyContent={true}>
        <CategoryCreationForm
          ref="categoryCreationForm"
          error={categoryCreationError}
          loading={categoryCreationInProgress} />
      </Dialog>
    )
  }

  renderDifficultyCreationDialog() {
    const {
      difficultyCreationError,
      difficultyCreationInProgress,
      difficultyCreationDialogVisible,
    } = this.state

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        disabled={difficultyCreationInProgress}
        onTouchTap={::this.onCancelDifficultyClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        disabled={difficultyCreationInProgress}
        onTouchTap={::this.onCreateDifficultyClicked} />
    ]

    return (
      <Dialog
        title="Create New Difficulty"
        actions={dialogActions}
        modal={true}
        open={difficultyCreationDialogVisible}
        onRequestClose={::this.onCancelDifficultyClicked}
        autoScrollBodyContent={true}>
        <DifficultyCreationForm
          ref="difficultyCreationForm"
          error={difficultyCreationError}
          loading={difficultyCreationInProgress} />
      </Dialog>
    )
  }

  render() {
    const { loading, categories, difficulties } = this.props
    const {
      requiresCalculator,
      selectedCategoryId,
      selectedDifficultyId,
      selectedQuestionType,
    } = this.state

    const categoryMenuItems = categories.map(category => {
      return (
        <MenuItem
          key={category.id}
          value={category.id}
          primaryText={category.name} />
      )
    })
    const difficultyMenuItems = difficulties.map(difficulty => {
      return (
        <MenuItem
          key={difficulty.id}
          value={difficulty.id}
          style={{ color: `#${difficulty.color}` }}
          primaryText={difficulty.name} />
      )
    })

    return (
      <div className={style.main}>
        <form ref="form" encType="multipart/form-data">
          <FormLoader visible={loading} />
          {this.renderError()}

          <div>
            <div className={style.label}>Question Requirements</div>
            <Checkbox
              label="Requires Calculator"
              checked={requiresCalculator}
              onCheck={::this.onRequiresCalculatorChecked} />
          </div>
          <SelectField
            value={selectedQuestionType}
            onChange={::this.onQuestionTypeChanged}
            fullWidth={true}
            floatingLabelText="Question Type">
            <MenuItem
              key={questionTypeMultipleChoice}
              value={questionTypeMultipleChoice}
              primaryText="Multiple Choice" />
            <MenuItem
              key={questionTypeNumericAnswer}
              value={questionTypeNumericAnswer}
              primaryText="Numeric Answer" />
          </SelectField>

          {this.renderAnswerField()}

          <div className={style.pictures}>
            <div>
              <div className={style.label}>Question Picture</div>
              <input
                name="questionPicture"
                type="file"
                accept="image/*;capture=camera" />
            </div>
            <div>
              <div className={style.label}>Question Answer</div>
              <input
                name="answerPicture"
                type="file"
                accept="image/*;capture=camera" />
            </div>
          </div>
          <div className={style.select}>
            <SelectField
              value={selectedCategoryId}
              hintText="The category of this question"
              disabled={categoryMenuItems.length < 1}
              onChange={::this.onCategoryChanged}
              fullWidth={true}
              floatingLabelText="Category">
              {categoryMenuItems}
            </SelectField>
            <FlatButton
              label="add"
              style={{ marginLeft: '1.2rem' }}
              primary={true}
              onClick={::this.onOpenCategoryCreationDialogClicked} />
          </div>
          <div className={style.select}>
            <SelectField
              value={selectedDifficultyId}
              hintText="The hardness of this question"
              disabled={difficultyMenuItems.length < 1}
              onChange={::this.onDifficultyChanged}
              fullWidth={true}
              floatingLabelText="Difficulty">
              {difficultyMenuItems}
            </SelectField>
            <FlatButton
              label="add"
              style={{ marginLeft: '1.2rem' }}
              primary={true}
              onClick={::this.onOpenDifficultyCreationDialogClicked} />
          </div>
          <TextField
            name="source"
            hintText="e.g. Barron's Book"
            fullWidth={true}
            floatingLabelText="Question Source" />
          <TextField
            name="sourcePage"
            fullWidth={true}
            floatingLabelText="Question Source Page" />

          {this.renderHiddenFields()}
          {this.renderCategoryCreationDialog()}
          {this.renderDifficultyCreationDialog()}
        </form>
      </div>
    )
  }
}

export default QuestionCreationForm
