
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import React, { Component } from 'react'
import { bindActionCreators } from 'redux'

import style from './style.css'
import actions from 'actions'
import PracticeSkeleton from 'components/PracticeSkeleton'
import QuizGenerationQAForm from 'components/QuizGenerationQAForm'
import { extractErrorFromResultingActions } from 'utils'

const GENERATION_STRATEGY_REALISTIC   = 'realistic'
const GENERATION_STRATEGY_BY_CATEGORY = 'generate_by_category'
const GENERATION_STRATEGY_BY_SOURCE   = 'generate_by_source'
const GENERATION_STRATEGY_BY_WEAKNESS = 'generate_by_weakness'

class QuizGenerationQAPage extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    categories:         React.PropTypes.array.isRequired,
    sources:            React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  }

  state = {
    loadingError:             null,
    isDataLoading:            false,
    isSourceDataLoading:      false,
    generateDiagnostic:       false,
    quizGenerationError:      null,
    quizGenerationInProgress: false,
    generationStrategy:       GENERATION_STRATEGY_BY_SOURCE,
    quizTitle:                "Quality Assured",
  }

  componentWillMount() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }

  loadData() {
    const { loadCategories, loadSources } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, isSourceDataLoading: true, loadingError: null })


    // Re-load everything.
    loadSources()
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
        if (error) {
          this.setState({
            loadingError:   error,
            isSourceDataLoading:  false,
          })
        } else {
          this.setState({ isSourceDataLoading: false })
        }
      })

    loadCategories()
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
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

  onStartClicked() {
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

          this.context.router.push(`/quiz/${resultingAction.payload.id}/take`)
        }
      })
  }

  render() {
    const { actions, categories, sources } = this.props
    const {
      isDataLoading,
      isSourceDataLoading,
      quizGenerationError,
      quizGenerationInProgress,
      generationStrategy,
      quizTitle,
    } = this.state
    return (
      <PracticeSkeleton title="Quizzical" subtitle={quizTitle}>
        <div className={style.main}>
          <div className={style.middle}>
            <QuizGenerationQAForm
              ref="generationForm"
              error={quizGenerationError}
              loading={quizGenerationInProgress}
              categories={categories}
              sources={sources}
              outsidePopup={true}
              genStratParam={generationStrategy}  />
          </div>
          <div className={style.bottom}>
            <RaisedButton
              label="Start"
              onClick={::this.onStartClicked}
              disabled={isDataLoading&&isSourceDataLoading}
              labelColor="#754aec"
              backgroundColor="#ffffff" />
          </div>
        </div>
      </PracticeSkeleton>
    )
  }
}

const reduxify = connect(
  (state, props) => ({
    categories:               state.category.list,
    dataShouldBeLoaded:       !state.category.loaded,
    sources:                  state.source.list,
    sourceDataShouldBeLoaded: !state.source.loaded,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quizgeneration, dispatch),
      bindActionCreators(actions.quiz, dispatch),
      bindActionCreators(actions.category, dispatch),
      bindActionCreators(actions.source, dispatch)),
  }))

export default reduxify(QuizGenerationQAPage)
