
import { connect } from 'react-redux'
import RaisedButton from 'material-ui/RaisedButton'
import { bindActionCreators } from 'redux'
import React, { Component, PropTypes } from 'react'

import style from './style.css'
import actions from 'actions'
import PracticeSkeleton from 'components/PracticeSkeleton'
import QuizGenerationForm from 'components/QuizGenerationForm'
import { extractErrorFromResultingActions } from 'utils'

const GENERATION_STRATEGY_REALISTIC   = 'realistic'
const GENERATION_STRATEGY_BY_CATEGORY = 'generate_by_category'
const GENERATION_STRATEGY_BY_WEAKNESS = 'generate_by_weakness'

class QuizAutoGenerationPage extends Component {
  static propTypes = {
    history:            PropTypes.object.isRequired,
    actions:            PropTypes.object.isRequired,
    categories:         PropTypes.array.isRequired,
    dataShouldBeLoaded: PropTypes.bool.isRequired,
  }

  state = {
    loadingError:             null,
    isDataLoading:            false,
    generateDiagnostic:       false,
    quizGenerationError:      null,
    quizGenerationInProgress: false,
    generationStrategy:       GENERATION_STRATEGY_BY_WEAKNESS,
    quizTitle:                null,
  }

  componentWillMount() {
    if (this.props.dataShouldBeLoaded) this.loadData()
  }

  loadData() {
    const { getQuizGenerationType } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    getQuizGenerationType()
      .then(resultingActions => {
        const error = extractErrorFromResultingActions(resultingActions)
        if (error) {
          this.setState({
            loadingError:   error,
            isDataLoading:  false,
          })
        } else {
          const genStrat = resultingActions.payload.gentype;
          const title = (genStrat === GENERATION_STRATEGY_REALISTIC) ?
            "Click Start to begin your diagnostic quiz":"Choose your quiz size"
          this.setState({
            isDataLoading:       false,
            generationStrategy:  genStrat,
            quizTitle: title,
          })
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

          this.props.history.push(`/quiz/${resultingAction.payload.id}`)
        }
      })
  }

  render() {
    const { actions, categories } = this.props
    const {
      isDataLoading,
      quizGenerationError,
      quizGenerationInProgress,
      generationStrategy,
      quizTitle,
    } = this.state
    return (
      <PracticeSkeleton
        title="Choose Your Quiz"
        action="Start Now"
        animated={true}
        actionDisabled={isDataLoading}
        animationDelay={100}
        onActionClicked={::this.onStartClicked}>
        <QuizGenerationForm
          ref="generationForm"
          error={quizGenerationError}
          loading={quizGenerationInProgress}
          categories={categories}
          imposedGenerationStrategy={generationStrategy}  />
      </PracticeSkeleton>
    )
  }
}

const reduxify = connect(
  (state, props) => ({
    categories:         state.category.list,
    dataShouldBeLoaded: !state.category.loaded,
  }),
  (dispatch, props) => ({
    actions: Object.assign(
      {},
      bindActionCreators(actions.quizgeneration, dispatch),
      bindActionCreators(actions.quiz, dispatch),
      bindActionCreators(actions.category, dispatch))
  }))

// Connect the quiz auto generation page to the store.
const QuizAutoGenerationPageWithRedux = reduxify(QuizAutoGenerationPage)
// Connect the quiz auto generation page to the router so that it can perform
// history operations.
const QuizAutoGenerationPageWithReduxWithRouter = withRouter(QuizAutoGenerationPageWithRedux)

export default QuizAutoGenerationPageWithReduxWithRouter
