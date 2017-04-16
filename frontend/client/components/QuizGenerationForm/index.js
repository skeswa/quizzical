
import Slider from 'material-ui/Slider'
import autobind from 'autobind-decorator'
import ReactDOM from 'react-dom'
import MenuItem from 'material-ui/MenuItem'
import TextField from 'material-ui/TextField'
import classNames from 'classnames'
import SelectField from 'material-ui/SelectField'
import React, { Component } from 'react'
import CategoryCreationForm from 'components/CategoryCreationForm'
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton'

import style from './index.css'
import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'
import QuizLengthPager from 'components/QuizLengthPager'

const QUIZ_LENGTH_NAMES = [
  'Very Short',
  'Short',
  'Medium',
  'Long',
  'Very Long',
  'Full Size'
]
const MIN_QUESTIONS_COUNT = 10
const MAX_QUESTIONS_COUNT = 20
const CATEGORY_SELECT_STYLE = { maxWidth: '100%' }
const QUIZ_LENGTH_PAGE_STYLE = { transform: 'scale(1.1)' }

const GENERATION_STRATEGY_REALISTIC   = 'realistic'
const GENERATION_STRATEGY_BY_CATEGORY = 'generate_by_category'
const GENERATION_STRATEGY_BY_WEAKNESS = 'generate_by_weakness'

class QuizGenerationForm extends Component {
  static propTypes = {
    error:                      React.PropTypes.any,
    loading:                    React.PropTypes.bool.isRequired,
    categories:                 React.PropTypes.array.isRequired,
    imposedGenerationStrategy:  React.PropTypes.string.isRequired,
  }

  state = {
    selectedLengthIndex:            2 /* Medium is the default */,
    selectedCategoryId:             null,
    generationStrategy:             GENERATION_STRATEGY_BY_WEAKNESS,
    categorySelectHeight:           7.2 /* in rem */,
    generationStrategyGroupHeight:  7.2 /* in rem */,
  }

  componentDidMount() {
    const { categories } = this.props

    if (categories && categories.length > 0) {
      this.setState({ selectedCategoryId: categories[0].id })
    }
  }

  componentWillReceiveProps(nextProps) {
    const { error: nextError, categories: nextCategories } = nextProps
    const { error: currentError, categories: currentCategories } = this.props

    if (currentError !== nextError) {
      // Make sure that the error is visible.
      this.scrollToTop()
    }

    if ((currentCategories ? currentCategories.length < 1 : true)
        && nextCategories
        && nextCategories.length > 0) {
      // Set the default category.
      this.setState({ selectedCategoryId: nextCategories[0].id })
    }
  }

  getJSON = () => {
    const { imposedGenerationStrategy } = this.props
    const { selectedLengthIndex, selectedCategoryId, generationStrategy } = this.state

    // Turn quiz length into a number of questions.
    const questionCount =
      Math.round(((MAX_QUESTIONS_COUNT - MIN_QUESTIONS_COUNT) *
      (selectedLengthIndex / (QUIZ_LENGTH_NAMES.length - 1))) + MIN_QUESTIONS_COUNT)

    return Object.assign(
        { quizSize: questionCount },
        // If there is an imposed generation strategy, then don't include the
        // selected category, and use the imposed gen. strat. instead.
        imposedGenerationStrategy
            ? { generatorType: imposedGenerationStrategy }
            : {
                generatorType: generationStrategy,
                problemCategoryId: selectedCategoryId,
              })
  }

  scrollToTop() {
    const node = ReactDOM.findDOMNode(this)
    const parentNode = node.parentNode
    parentNode.scrollTop = 0
  }

  @autobind
  onCategoryChanged(e, i, selectedCategoryId) {
    this.setState({ selectedCategoryId })
  }

  @autobind
  onSelectedLengthChanged(selectedLengthIndex) {
    this.setState({ selectedLengthIndex })
  }

  @autobind
  onCategorySelectMounted(categorySelect) {
    this.setState({ categorySelectHeight: categorySelect.offsetHeight })
  }

  @autobind
  onGenerationStrategyChanged(e, generationStrategy) {
    this.setState({ generationStrategy })
  }

  renderError() {
    const { error } = this.props

    if (error) {
      const message = error.error
        ? error.error
        : error

      return (
        <div className={style.formErrorWrapper}>
          <FormError
            title="Failed to create category"
            message={message} />
        </div>
      )
    }

    return null
  }

  render() {
    const { loading, categories, imposedGenerationStrategy } = this.props
    const {
      selectedLengthIndex,
      generationStrategy,
      selectedCategoryId,
      categorySelectHeight,
      generationStrategyGroupHeight,
    } = this.state

    const categoryMenuItems = categories.map(
      category =>
        <MenuItem
          key={category.id}
          value={category.id}
          primaryText={category.name} />)
    const categorySelectWrapperStyle = {
      height:
        !imposedGenerationStrategy && generationStrategy === GENERATION_STRATEGY_BY_CATEGORY
            ? `${categorySelectHeight}rem`
            : '0'
    }
    const generationStrategyGroupWrapperStyle = {
      height:
        !imposedGenerationStrategy
            ? `${generationStrategyGroupHeight}rem`
            : '0'
    }

    return (
      <div className={style.main}>
        <FormLoader visible={loading} />

        {this.renderError()}

        <div className={style.quizLengthPagerWrapper}>
          <QuizLengthPager
            style={QUIZ_LENGTH_PAGE_STYLE}
            lengths={QUIZ_LENGTH_NAMES}
            selectedLengthIndex={selectedLengthIndex}
            onSelectedLengthChanged={this.onSelectedLengthChanged} />
        </div>
        <div
          style={generationStrategyGroupWrapperStyle}
          className={style.generationStrategyGroupWrapper}>
            <div className={style.generationStrategyLabel}>
              Generation Strategy
            </div>
            <RadioButtonGroup
              name="generation-strategy"
              onChange={this.onGenerationStrategyChanged}
              valueSelected={generationStrategy}>
              <RadioButton
                value={GENERATION_STRATEGY_BY_CATEGORY}
                label="Category Focus" />
              <RadioButton
                value={GENERATION_STRATEGY_REALISTIC}
                label="Realistic Practice" />
              <RadioButton
                value={GENERATION_STRATEGY_BY_WEAKNESS}
                label="Weakness Training" />
            </RadioButtonGroup>
        </div>
        <div
          style={categorySelectWrapperStyle}
          className={style.categorySelectWrapper}>
          <SelectField
            ref="categorySelect"
            style={CATEGORY_SELECT_STYLE}
            value={selectedCategoryId}
            hintText="The category to focus"
            disabled={categoryMenuItems.length < 1}
            onChange={this.onCategoryChanged}
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
