
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
import DifficultyCreationForm from 'components/DifficultyCreationForm'

const questionTypeNumericAnswer = 'numericAnswer'
const questionTypeMultipleChoice  = 'multipleChoice'

class QuestionCreationForm extends Component {
  static propTypes = {
    categories:               React.PropTypes.array.isRequired,
    difficulties:             React.PropTypes.array.isRequired,
    createCategory:           React.PropTypes.func.isRequired,
    questionLoading:          React.PropTypes.bool.isRequired,
    categoryLoading:          React.PropTypes.bool.isRequired,
    createDifficulty:         React.PropTypes.func.isRequired,
    difficultyLoading:        React.PropTypes.bool.isRequired,
    questionCreationError:    React.PropTypes.any,
    categoryCreationError:    React.PropTypes.any,
    difficultyCreationError:  React.PropTypes.any,
  }

  state = {
    selectedCategory:                 null,
    selectedDifficulty:               null,
    requiresCalculator:               false,
    selectedQuestionType:             null,
    difficultyCreationDialogVisible:  false,
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.questionCreationError !== nextProps.questionCreationError) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }
  }

  onCategoryChanged(e, i, value) {
    this.setState({ selectedCategory: value })
  }

  onDifficultyChanged(e, i, value) {
    this.setState({ selectedDifficulty: value })
  }

  onQuestionTypeChanged(e, i, value) {
    this.setState({ selectedQuestionType: value })
  }

  onRequiresCalculatorChecked(e, checked) {
    this.setState({ requiresCalculator: checked })
  }

  onCreateDifficultyClicked() {
    const json = this.refs.difficultyCreationForm.getJSON()
    this.props.actions.createDifficulty(json)
  }

  onCancelDifficultyClicked() {
    this.setState({ difficultyCreationDialogVisible: false })
  }

  onOpenDifficultyCreationDialogClicked() {
    this.setState({ difficultyCreationDialogVisible: true })
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
      selectedCategory,
      selectedDifficulty,
      requiresCalculator,
      selectedQuestionType,
    } = this.state

    const categoryId = selectedCategory ? selectedCategory.id : null
    const difficultyId = selectedDifficulty ? selectedDifficulty.id : null
    const multipleChoice = selectedQuestionType
      ? selectedQuestionType === questionTypeMultipleChoice
      : null

    return [
      categoryId
        ? <input
            key="categoryId"
            type="hidden"
            name="categoryId"
            value={categoryId} />
        : null,
      difficultyId
        ? <input
            key="difficultyId"
            type="hidden"
            name="difficultyId"
            value={difficultyId} />
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
    const { questionCreationError } = this.props

    if (questionCreationError) {
      const message = questionCreationError.error
        ? questionCreationError.error
        : questionCreationError

      return (
        <FormError
          title="Failed to create question"
          message={message} />
      )
    }

    return null
  }

  renderDifficultyCreationDialog() {
    const { difficultyCreationDialogVisible } = this.state
    const {
      difficultyLoading,
      difficultyCreationError,
    } = this.props

    const dialogActions = [
      <FlatButton
        label="Cancel"
        primary={true}
        disabled={difficultyLoading}
        onTouchTap={::this.onCancelDifficultyClicked} />,
      <RaisedButton
        label="Create"
        primary={true}
        disabled={difficultyLoading}
        onTouchTap={::this.onCreateDifficultyClicked} />
    ]

    return (
      <Dialog
        title="Create New Difficulty"
        actions={dialogActions}
        modal={true}
        open={difficultyCreationDialogVisible}
        onRequestClose={::this.onCancelDifficultyClicked}>
        <DifficultyCreationForm
          ref="difficultyCreationForm"
          error={difficultyCreationError}
          loading={difficultyLoading} />
      </Dialog>
    )
  }

  render() {
    const {
      categories,
      difficulties,
      questionLoading,
      categoryLoading,
      difficultyLoading,
    } = this.props
    const {
      selectedCategory,
      selectedDifficulty,
      requiresCalculator,
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
    const difficultyMenuItems = categories.map(difficulty => {
      return (
        <MenuItem
          key={difficulty.id}
          value={difficulty.id}
          style={difficulty.color}
          primaryText={difficulty.name} />
      )
    })

    return (
      <div className={style.main}>
        <form ref="form" encType="multipart/form-data">
          <FormLoader visible={questionLoading} />
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
              value={selectedCategory}
              hintText="The category of this question"
              disabled={difficultyMenuItems.length < 1}
              onChange={::this.onCategoryChanged}
              fullWidth={true}
              floatingLabelText="Category">
              {categoryMenuItems}
            </SelectField>
            <FlatButton
              label="add"
              style={{ marginLeft: '1.2rem' }}
              primary={true} />
          </div>
          <div className={style.select}>
            <SelectField
              value={selectedDifficulty}
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
          {this.renderDifficultyCreationDialog()}
        </form>
      </div>
    )
  }
}

export default QuestionCreationForm
