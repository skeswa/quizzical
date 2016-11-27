
import Slider from 'material-ui/Slider'
import ReactDOM from 'react-dom'
import MenuItem from 'material-ui/MenuItem'
import TextField from 'material-ui/TextField'
import SelectField from 'material-ui/SelectField'
import React, { Component } from 'react'
import CategoryCreationForm from 'components/CategoryCreationForm'
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton'

import style from './style.css'
import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'

const QUIZ_LENGTH_NAMES = [
  'Very Short',
  'Short',
  'Medium',
  'Long',
  'Very Long'
]
const MIN_QUESTIONS_COUNT = 3
const MAX_QUESTIONS_COUNT = 25

class QuizGenerationForm extends Component {
  static propTypes = {
    error:          React.PropTypes.any,
    loading:        React.PropTypes.bool.isRequired,
    categories:     React.PropTypes.array.isRequired,
  }

  state = {
    quizLength:         1,
    selectedCategoryId: null,
    generationStrategy: 'generate_by_weakness',
  }

  onCategoryChanged(e, i, selectedCategoryId) {
    this.setState({ selectedCategoryId })
  }

  onQuizLengthChanged(e, quizLength) {
    this.setState({ quizLength })
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.error !== nextProps.error) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }
  }

  onGenerationStrategyChanged(e, generationStrategy) {
    this.setState({ generationStrategy })
  }

  getJSON = () => {
    const { quizLength, selectedCategoryId, generationStrategy } = this.state

    // Turn quiz length into a number of questions.
    const questionCount =
      ((MAX_QUESTIONS_COUNT - MIN_QUESTIONS_COUNT) *
      (quizLength / (QUIZ_LENGTH_NAMES.length - 1))) + MIN_QUESTIONS_COUNT

    return {
      quizSize: questionCount,
      generatorType: generationStrategy,
      problemCategoryId: selectedCategoryId,
    }
  }

  scrollToTop() {
    const node = ReactDOM.findDOMNode(this)
    const parentNode = node.parentNode
    parentNode.scrollTop = 0
  }

  renderError() {
    const { error } = this.props

    if (error) {
      const message = error.error
        ? error.error
        : error

      return (
        <FormError
          title="Failed to create category"
          message={message} />
      )
    }

    return null
  }

  render() {
    const { loading, categories } = this.props
    const { quizLength, generationStrategy, selectedCategoryId } = this.state

    const categoryMenuItems = categories.map(category => {
      return (
        <MenuItem
          key={category.id}
          value={category.id}
          primaryText={category.name} />
      )
    })

    return (
      <div className={style.main}>
        <FormLoader visible={loading} />
        {this.renderError()}

        <div className={style.quizLengthLabel}>
          <div className={style.quizLengthLabelTitle}>Quiz Length:</div>
          <div className={style.quizLengthLabelValue}>
            {QUIZ_LENGTH_NAMES[quizLength]}
          </div>
        </div>
        <Slider
          min={0}
          max={QUIZ_LENGTH_NAMES.length - 1}
          step={1}
          value={quizLength}
          required={false}
          onChange={::this.onQuizLengthChanged} />

        <div className={style.generationStrategyLabel}>Generation Strategy</div>
        <RadioButtonGroup
          name="generation-strategy"
          onChange={::this.onGenerationStrategyChanged}
          valueSelected={generationStrategy}>
          <RadioButton
            value="generate_by_category"
            label="Category Focus" />
          <RadioButton
            value="realistic"
            label="Realistic Practice" />
          <RadioButton
            value="generate_by_weakness"
            label="Weakness Training" />
        </RadioButtonGroup>

        <div
          style={{
            display: generationStrategy === 'generate_by_category'
              ? 'block'
              : 'none'
          }}>
          <SelectField
            value={selectedCategoryId}
            hintText="The category to focus"
            disabled={categoryMenuItems.length < 1}
            onChange={::this.onCategoryChanged}
            fullWidth={true}
            floatingLabelText="Category to Focus">
            {categoryMenuItems}
          </SelectField>
        </div>
      </div>
    )
  }
}

export default QuizGenerationForm
