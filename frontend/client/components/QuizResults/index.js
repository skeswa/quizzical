
import classNames from 'classnames'
import RefreshIndicator from 'material-ui/RefreshIndicator'
import React, { Component } from 'react'

import style from './style.css'
import PictureTabs from './picture-tabs'
import { hsvToRgb } from 'utils/color'

// In seconds. 38 questions in 55 minutes.
const IDEAL_CALCULATOR_QUESTION_DURATION = (55 * 60) / 38
// In seconds. 20 questions in 25 minutes.
const IDEAL_NO_CALCULATOR_QUESTION_DURATION = (25 * 60) / 20

const BAD_DURATION_COLOR = { h: 0, s: 0.797979797979798, v: 0.7764705882352941 }
const GOOD_DURATION_COLOR = {
  h: 0.34177215189873417,
  s: 0.632,
  v: 0.49019607843137253,
}
const DURATION_RANGE_RATIO = 0.8
const DURATION_DESCRIPTIONS = [
  'really slow!',
  'slow.',
  'a little slow.',
  'just alright.',
  'ideal.',
  'good.',
  'really great!',
  'amazing!'
]

const CORRECT_SUBHEADINGS = [
  'Ayy! Nice work!',
  'You did it!',
  'Good work on this one.',
  'You really showed up, nice.',
  'Congrats on getting this right!',
  'Good show.',
]
const SKIPPED_SUBHEADINGS = [
  'Let\'s try to answer this question next time.',
  'Hmm. Looks like you didn\'t answer this.',
  'Nothing to see here.',
]
const INCORRECT_SUBHEADINGS = [
  'You messed up.',
  'Clearly, this went poorly.',
  'Not impressed. Give it another shot!',
  'Are you feeling okay?',
  'Is that all you\'ve got?',
  'Better luck next time.',
  'Try much? lol.',
]

const OUTCOME_CORRECT = 'correct'
const OUTCOME_SKIPPED = 'skipped'
const OUTCOME_INCORRECT = 'incorrect'

const SEGMENT_ICON_CLASSNAME = `material-icons ${style.summarySegmentIcon}`
const QUESTION_BUTTON_ICON_CLASSNAME = `material-icons ${style.questionButtonIcon}`
const RESPONSE_DETAILS_STAT_ICON_CLASSNAME = `material-icons ${style.responseDetailsStatIcon}`

// TODO(skeswa): Hmm. Question picture needs to be extracted to its own
// component.
const BASE_QUESTION_PICTURE_URL = '/api/problems/pictures'

class QuizResults extends Component {
  static propTypes = {
    results: React.PropTypes.object.isRequired,
  }

  state = {
    selectedQuestionOrdinal: 0,
  }

  getCounts(responses) {
    const counts = { correctCount: 0, skippedCount: 0, incorrectCount: 0 }

    responses.forEach(response => {
      switch (this.getOutcome(response)) {
        case OUTCOME_CORRECT:
          return counts.correctCount++
        case OUTCOME_SKIPPED:
          return counts.skippedCount++
        case OUTCOME_INCORRECT:
          return counts.incorrectCount++
      }
    })

    return counts
  }

  getOutcome(response) {
    const { correct, skipped } = response

    if (skipped) return OUTCOME_SKIPPED
    if (correct) return OUTCOME_CORRECT
    return OUTCOME_INCORRECT
  }

  toPictureURL(pictureId) {
    return `${BASE_QUESTION_PICTURE_URL}/${pictureId}.png`
  }

  getDurationColor(duration, usedCalculator) {
    const performanceIndex = this.getDurationPerformanceIndex(
      duration,
      usedCalculator)

    const h1 = GOOD_DURATION_COLOR.h
    const h2 = BAD_DURATION_COLOR.h
    const s1 = GOOD_DURATION_COLOR.s
    const s2 = BAD_DURATION_COLOR.s
    const v1 = GOOD_DURATION_COLOR.v
    const v2 = BAD_DURATION_COLOR.v

    return hsvToRgb(
      h1 + ((h2 - h1) * performanceIndex),
      s1 + ((s2 - s1) * performanceIndex),
      v1 + ((v2 - v1) * performanceIndex))
  }

  outcomeIconNameOf(outcome) {
    switch (outcome) {
      case OUTCOME_CORRECT:
        return 'done'
      case OUTCOME_SKIPPED:
        return 'skip_next'
      case OUTCOME_INCORRECT:
        return 'clear'
    }

    return ''
  }

  getOutcomeHeading(outcome, ordinal) {
    let status
    switch (outcome) {
      case OUTCOME_CORRECT:
        status = 'answered correctly'
        break
      case OUTCOME_SKIPPED:
        status = 'skipped'
        break
      case OUTCOME_INCORRECT:
        status = 'answered incorrectly'
        break
      default:
        return ''
    }

    return `Question #${ordinal + 1} was ${status}.`
  }

  getOutcomeSubHeading(outcome, quizProblemId) {
    let subHeadings
    switch (outcome) {
      case OUTCOME_CORRECT:
        subHeadings = CORRECT_SUBHEADINGS
        break
      case OUTCOME_SKIPPED:
        subHeadings = SKIPPED_SUBHEADINGS
        break
      case OUTCOME_INCORRECT:
        subHeadings = INCORRECT_SUBHEADINGS
        break
      default:
        return ''
    }

    return subHeadings[quizProblemId % subHeadings.length]
  }

  sortResponsesByOrdinal(responses) {
    return responses.slice(0).sort(::this.compareResponsesByOrdinal)
  }

  getDurationDescription(duration, usedCalculator) {
    const performanceIndex = this.getDurationPerformanceIndex(
      duration,
      usedCalculator)
    const descriptionIndex = (DURATION_DESCRIPTIONS.length - 1)
      - Math.round(performanceIndex * (DURATION_DESCRIPTIONS.length - 1))

    return DURATION_DESCRIPTIONS[descriptionIndex]
  }

  compareResponsesByOrdinal(a, b) {
    if (a.quizProblem.ordinal < b.quizProblem.ordinal) return -1
    if (a.quizProblem.ordinal > b.quizProblem.ordinal) return 1
    return 0
  }

  getDurationPerformanceIndex(duration, usedCalculator) {
    const denominator = usedCalculator
      ? IDEAL_CALCULATOR_QUESTION_DURATION
      : IDEAL_NO_CALCULATOR_QUESTION_DURATION
    const performanceRatio = duration / denominator
    const boundedPerformanceRatio =
      performanceRatio > (1 + DURATION_RANGE_RATIO)
        ? 1 + DURATION_RANGE_RATIO
        : performanceRatio < (1 - DURATION_RANGE_RATIO)
          ? 1 - DURATION_RANGE_RATIO
          : performanceRatio

    return (boundedPerformanceRatio - (1 - DURATION_RANGE_RATIO))
      / (2 * DURATION_RANGE_RATIO)
  }

  onSelectedQuestionOrdinalChanged(selectedQuestionOrdinal) {
    this.setState({ selectedQuestionOrdinal })
  }

  renderResponseDetails(sortedResponses, selectedQuestionOrdinal) {
    const response = sortedResponses
      .filter(({ quizProblem: { ordinal } }) =>
        ordinal === selectedQuestionOrdinal)
      .reduce((a, b) => b ? b : a)

    if (!response) return null

    const {
      response: questionRepsonse,
      quizProblem: {
        id:       quizProblemId,
        ordinal:  questionOrdinal,
        problem:  {
          answer:                 questionAnswer,
          difficulty:             { name: difficulty, color: difficultyColor },
          answerPicture:          { id: answerPictureId },
          questionPicture:        { id: questionPictureId },
          requiresCalculator:     usedCalculator,
          sourceIndexWithinPage:  questionNumber,
        },
      },
      secondsElapsed: duration,
    } = response

    const outcome = this.getOutcome(response)
    const heading = this.getOutcomeHeading(outcome, selectedQuestionOrdinal)
    const subHeading = this.getOutcomeSubHeading(outcome, quizProblemId)
    const durationColor = this.getDurationColor(duration, usedCalculator)
    const answerPictureStyle = {
      backgroundImage: `url(${this.toPictureURL(answerPictureId)})`,
    }
    const questionPictureStyle = {
      backgroundImage: `url(${this.toPictureURL(questionPictureId)})`,
    }
    const durationDescription = this.getDurationDescription(
      duration,
      usedCalculator)
    const responseDetailsClassNames = classNames(style.responseDetails, {
      [style.responseDetails__correct]:   outcome === OUTCOME_CORRECT,
      [style.responseDetails__skipped]:   outcome === OUTCOME_SKIPPED,
      [style.responseDetails__incorrect]: outcome === OUTCOME_INCORRECT,
    })
    const responseDetailsStatHighlightStyle = { color: durationColor }

    return (
      <div className={responseDetailsClassNames}>
        <div className={style.responseDetailsInfo}>
          <div className={style.responseDetailsHeading}>{heading}</div>
          <div className={style.responseDetailsSubHeading}>{subHeading}</div>
          <div className={style.responseDetailsStats}>
            <div className={style.responseDetailsStat}>
              <i className={RESPONSE_DETAILS_STAT_ICON_CLASSNAME}>alarm</i>
              <div className={style.responseDetailsStatText}>
                <span>You took&nbsp;</span>
                <span
                  style={responseDetailsStatHighlightStyle}
                  className={style.responseDetailsStatHighlight}>
                  {duration} seconds
                </span>
                <span>, which is&nbsp;</span>
                <span
                  style={responseDetailsStatHighlightStyle}
                  className={style.responseDetailsStatHighlight}>
                  {durationDescription}
                </span>
              </div>
            </div>
            <div className={style.responseDetailsStat}>
              <i className={RESPONSE_DETAILS_STAT_ICON_CLASSNAME}>reply</i>
              <div className={style.responseDetailsStatText}>
                {
                  outcome === OUTCOME_SKIPPED
                    ? <span>You didn't provide an answer</span>
                    : <span>
                        <span>You answered&nbsp;</span>
                        <span className={style.responseDetailsStatHighlight}>
                          {questionRepsonse}
                        </span>
                      </span>
                }
                {
                  outcome === OUTCOME_CORRECT
                    ? <span>
                        <span>, which is&nbsp;</span>
                        <span className={style.responseDetailsStatHighlight}>
                          correct.
                        </span>
                      </span>
                    : <span>
                        <span>, but the correct answer is&nbsp;</span>
                        <span className={style.responseDetailsStatHighlight}>
                          {questionAnswer}
                        </span>
                        <span>.</span>
                      </span>
                }
              </div>
            </div>
            <div className={style.responseDetailsStat}>
              <i className={RESPONSE_DETAILS_STAT_ICON_CLASSNAME}>equalizer</i>
              <div className={style.responseDetailsStatText}>
                <span>This question is considered&nbsp;</span>
                <span
                  style={{ color: difficultyColor }}
                  className={style.responseDetailsStatHighlight}>
                  {difficulty}
                </span>
                <span>.</span>
              </div>
            </div>
            <div className={style.responseDetailsStat}>
              <i className={RESPONSE_DETAILS_STAT_ICON_CLASSNAME}>
                format_list_numbered
              </i>
              <div className={style.responseDetailsStatText}>
                <span>You were asked to answer question&nbsp;</span>
                <span className={style.responseDetailsStatHighlight}>
                  {`#${questionNumber}`}
                </span>
                {
                  usedCalculator
                    ? <span>
                        <span>, which&nbsp;</span>
                        <span className={style.responseDetailsStatHighlight}>
                          required your calculator.
                        </span>
                      </span>
                    : <span>.</span>
                }
              </div>
            </div>
          </div>
        </div>
        <div className={style.responseDetailsPictures}>
          <PictureTabs
            answerPictureId={answerPictureId}
            questionPictureId={questionPictureId} />
        </div>
      </div>
    )
  }

  renderQuestionButtons(sortedResponses, selectedQuestionOrdinal) {
    return sortedResponses.map((response, i) => {
      const outcome = this.getOutcome(response)
      const { quizProblem: { ordinal } } = response
      const questionButtonClassNames = classNames(style.questionButton, {
        [style.questionButton__correct]:    outcome === OUTCOME_CORRECT,
        [style.questionButton__skipped]:    outcome === OUTCOME_SKIPPED,
        [style.questionButton__selected]:   ordinal === selectedQuestionOrdinal,
        [style.questionButton__incorrect]:  outcome === OUTCOME_INCORRECT,
      })

      return (
        <div
          key={ordinal}
          onClick={this.onSelectedQuestionOrdinalChanged.bind(this, ordinal)}
          className={questionButtonClassNames}>
          <i className={QUESTION_BUTTON_ICON_CLASSNAME}>
            {this.outcomeIconNameOf(outcome)}
          </i>
          <div className={style.questionButtonNumber}>#{ordinal + 1}</div>
        </div>
      )
    })
  }

  render() {
    const { results: { responses } } = this.props
    const { selectedQuestionOrdinal } = this.state
    const sortedResponses = this.sortResponsesByOrdinal(responses)
    const {
      correctCount,
      skippedCount,
      incorrectCount,
    } = this.getCounts(responses)
    const correctSummarySegmentClassNames = classNames(
      style.summarySegment,
      style.summarySegment__correct)
    const skippedSummarySegmentClassNames = classNames(
      style.summarySegment,
      style.summarySegment__skipped)
    const incorrectSummarySegmentClassNames = classNames(
      style.summarySegment,
      style.summarySegment__incorrect)

    return (
      <div className={style.main}>
        <div className={style.top}>
          <div className={style.summary}>
            <div className={correctSummarySegmentClassNames}>
              <i className={SEGMENT_ICON_CLASSNAME}>
                {this.outcomeIconNameOf(OUTCOME_CORRECT)}
              </i>
              <div className={style.summarySegmentCount}>{correctCount}</div>
            </div>
            <div className={skippedSummarySegmentClassNames}>
              <i className={SEGMENT_ICON_CLASSNAME}>
                {this.outcomeIconNameOf(OUTCOME_SKIPPED)}
              </i>
              <div className={style.summarySegmentCount}>{skippedCount}</div>
            </div>
            <div className={incorrectSummarySegmentClassNames}>
              <i className={SEGMENT_ICON_CLASSNAME}>
                {this.outcomeIconNameOf(OUTCOME_INCORRECT)}
              </i>
              <div className={style.summarySegmentCount}>{incorrectCount}</div>
            </div>
          </div>
        </div>
        <div className={style.bottom}>
          <div className={style.left}>
            {
              this.renderQuestionButtons(
                sortedResponses,
                selectedQuestionOrdinal)
            }
          </div>
          <div className={style.right}>
            {
              this.renderResponseDetails(
                sortedResponses,
                selectedQuestionOrdinal)
            }
          </div>
        </div>
      </div>
    )
  }
}

export default QuizResults
