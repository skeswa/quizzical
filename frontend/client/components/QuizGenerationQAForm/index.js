
import Slider from 'material-ui/Slider'
import ReactDOM from 'react-dom'
import MenuItem from 'material-ui/MenuItem'
import TextField from 'material-ui/TextField'
import classNames from 'classnames'
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import SelectField from 'material-ui/SelectField'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import React, { Component } from 'react'
import CategoryCreationForm from 'components/CategoryCreationForm'
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton'

import style from './style.css'
import FormError from 'components/FormError'
import FormLoader from 'components/FormLoader'

const DARK_MUI_THEME = getMuiTheme({
  slider: {
    trackColor: 'rgba(255, 255, 255, 0.6)',
    selectionColor: '#ffffff'
  },
  radioButton: {
    labelColor: '#ffffff',
    borderColor: '#ffffff',
    checkedColor: '#ffffff',
  },
  textField: {
    floatingLabelColor: '#ffffff',
    focusColor: '#ffffff',
    textColor: '#ffffff'
  }
});
const QUIZ_LENGTH_NAMES = [
  'Very Short',
  'Short',
  'Medium',
  'Long',
  'Very Long',
  'Full Size'
]
const MIN_QUESTIONS_COUNT = 3
const MAX_QUESTIONS_COUNT = 20
const CATEGORY_SELECT_STYLE = { maxWidth: '100%' }
const SOURCE_SELECT_STYLE   = { maxWidth: '100%' }

const GENERATION_STRATEGY_REALISTIC   = 'realistic'
const GENERATION_STRATEGY_BY_CATEGORY = 'generate_by_category'
const GENERATION_STRATEGY_BY_SOURCE   = 'generate_by_source'
const GENERATION_STRATEGY_BY_WEAKNESS = 'generate_by_weakness'

class QuizGenerationQAForm extends Component {
  static propTypes = {
    error:          React.PropTypes.any,
    loading:        React.PropTypes.bool.isRequired,
    categories:     React.PropTypes.array.isRequired,
    genStratParam:  React.PropTypes.string.isRequired,
    outsidePopup:   React.PropTypes.bool,
  }

  state = {
    quizLength:                     1,
    selectedCategoryId:             null,
    selectedSourceId:               null,
    generationStrategy:             GENERATION_STRATEGY_BY_SOURCE,
    categorySelectHeight:           72,
    sourceSelectHeight:             72,
    generationStrategyGroupHeight:  72,
    quizLengthSliderHeight:         142,
  }

  componentDidMount() {
    const { categories, sources, genStratParam } = this.props

    if (categories && categories.length > 0) {
      this.setState({ selectedCategoryId: categories[0].id })
    }

    if (sources && sources.length > 0) {
      this.setState({ selectedSourceId: sources[0].id })
    }

    this.setState({ generationStrategy: genStratParam })
  }

  componentWillReceiveProps(nextProps) {
    const { error: nextError,
            categories: nextCategories,
            sources: nextSources,
            genStratParam,
        } = nextProps
    const { error: currentError,
            categories: currentCategories,
            sources: currentSources,
        } = this.props

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

    if ((currentSources ? currentSources.length < 1 : true)
      && nextSources
      && nextSources.length > 0) {
      // Set the default category.
      this.setState({ selectedSourceId: nextSources[0].id })
    }

    this.setState({ generationStrategy: genStratParam })
  }

  getJSON = () => {
    const { quizLength,
            selectedCategoryId,
            selectedSourceId,
            generationStrategy,
        } = this.state

    // Turn quiz length into a number of questions.
    const questionCount =
      Math.round(((MAX_QUESTIONS_COUNT - MIN_QUESTIONS_COUNT) *
      (quizLength / (QUIZ_LENGTH_NAMES.length - 1))) + MIN_QUESTIONS_COUNT)

    return {
      quizSize: questionCount,
      generatorType: generationStrategy,
      problemCategoryId: selectedCategoryId,
      problemSourceId: selectedSourceId
    }
  }

  scrollToTop() {
    const node = ReactDOM.findDOMNode(this)
    const parentNode = node.parentNode
    parentNode.scrollTop = 0
  }

  onCategoryChanged(e, i, selectedCategoryId) {
    this.setState({ selectedCategoryId })
  }

  onSourceChanged(e, i, selectedSourceId) {
    this.setState({ selectedSourceId })
  }

  onQuizLengthChanged(e, quizLength) {
    this.setState({ quizLength })
  }

  onCategorySelectMounted(categorySelect) {
    debugger
    this.setState({ categorySelectHeight: categorySelect.offsetHeight })
  }

  onSourceSelectMounted(sourceSelect) {
    debugger
    this.setState({ sourceSelectHeight: sourceSelect.offsetHeight })
  }


  onGenerationStrategyChanged(e, generationStrategy) {
    this.setState({ generationStrategy })
  }

  renderError() {
    const { error, outsidePopup } = this.props

    if (error) {
      const message = error.error
        ? error.error
        : error

      return (
        <FormError
          title="Failed to create category"
          message={message}
          limitHeight={outsidePopup} />
      )
    }

    return null
  }

  render() {
    const { loading,
      categories,
      sources,
      outsidePopup,
      genStratParam,
    } = this.props
    const {
      quizLength,
      generationStrategy,
      selectedCategoryId,
      selectedSourceId,
      categorySelectHeight,
      sourceSelectHeight,
      generationStrategyGroupHeight,
      quizLengthSliderHeight,
    } = this.state

    const mainClasses = classNames(style.main, { [style.dark]: outsidePopup })
    const radioButtonStyle = outsidePopup
      ? { marginBottom: '1rem' }
      : undefined
    const selectFieldStyle = outsidePopup
      ? { color: '#ffffff' }
      : undefined
    const categoryMenuItems = categories.map(category => {
      return (
        <MenuItem
          key={category.id}
          value={category.id}
          primaryText={category.name} />
      )
    })
    const categorySelectWrapperStyle = {
      height: genStratParam === GENERATION_STRATEGY_BY_CATEGORY
        ? `${categorySelectHeight}px`
        : '0px'
    }
    const sourceMenuItems = sources.map(source => {
      return (
        <MenuItem
          key={source.id}
          value={source.id}
          primaryText={source.name} />
      )
    })
    const sourceSelectWrapperStyle = {
      height: genStratParam === GENERATION_STRATEGY_BY_SOURCE
        ? `${sourceSelectHeight}px`
        : '0px'
    }
    const generationStrategyGroupWrapperStyle = {
      height: false
        ? `${generationStrategyGroupHeight}px`
        : '0px'
    }
    const quizLengthSliderWrapperStyle = {
      height: true
        ? `${quizLengthSliderHeight}px`
        : '0px'
    }

    return (
      <MuiThemeProvider muiTheme={outsidePopup ? DARK_MUI_THEME : undefined}>
        <div className={mainClasses}>
          <FormLoader visible={loading} fullScreen={outsidePopup} />
          {this.renderError()}
          <div
            style={quizLengthSliderWrapperStyle}
            className={style.quizLengthSliderWrapper}>
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
          </div>
          <div
            style={generationStrategyGroupWrapperStyle}
            className={style.generationStrategyGroupWrapper}>
              <div className={style.generationStrategyLabel}>
                Generation Strategy
              </div>
              <RadioButtonGroup
                name="generation-strategy"
                onChange={::this.onGenerationStrategyChanged}
                valueSelected={generationStrategy}>
                <RadioButton
                  style={radioButtonStyle}
                  value={GENERATION_STRATEGY_BY_CATEGORY}
                  label="Category Focus" />
                <RadioButton
                  style={radioButtonStyle}
                  value={GENERATION_STRATEGY_BY_SOURCE}
                  label="Source Practice" />
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
              onChange={::this.onCategoryChanged}
              fullWidth={true}
              hintStyle={selectFieldStyle}
              labelStyle={selectFieldStyle}
              floatingLabelText="Category to Focus"
              floatingLabelStyle={selectFieldStyle}>
              {categoryMenuItems}
            </SelectField>
          </div>
          <div
            style={sourceSelectWrapperStyle}
            className={style.sourceSelectWrapper}>
            <SelectField
              ref="sourceSelect"
              style={SOURCE_SELECT_STYLE}
              value={selectedSourceId}
              hintText="The source to focus"
              disabled={sourceMenuItems.length < 1}
              onChange={::this.onSourceChanged}
              fullWidth={true}
              hintStyle={selectFieldStyle}
              labelStyle={selectFieldStyle}
              floatingLabelText="Source to QA On"
              floatingLabelStyle={selectFieldStyle}>
              {sourceMenuItems}
            </SelectField>
          </div>
        </div>
      </MuiThemeProvider>
    )
  }
}

export default QuizGenerationQAForm
