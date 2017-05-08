
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
import LessonGrid from 'components/LessonGrid'
import LessonTable from 'components/LessonTable'
import LessonCreationForm from 'components/LessonCreationForm'
import { extractErrorFromResultingActions } from 'utils'

class Lessons extends Component {
  static propTypes = {
    actions:            React.PropTypes.object.isRequired,
    sources:            React.PropTypes.array.isRequired,
    lessons:            React.PropTypes.array.isRequired,
    lessonStatuses:     React.PropTypes.array.isRequired,
    lessonTypes:        React.PropTypes.array.isRequired,
    dataShouldBeLoaded: React.PropTypes.bool.isRequired,
  }

  state = {
    loadingError:                 null,
    isDataLoading:                false,
    statusFilterId:               null,
    typeFilterId:                 null,
    sourceFilterId:               null,
    lessonCreationError:          null,
    lessonCreationInProgress:     false,
    lessonCreationDialogVisible:  false,
  }

  componentWillMount() {
    // Attempt to load lessons.
    if (this.props.dataShouldBeLoaded) {
      this.loadData()
    }
  }

  loadData() {
    const {
      loadUserLessons,
      loadLessonStatuses,
      loadLessonTypes
    } = this.props.actions

    // Indicate loading.
    this.setState({ isDataLoading: true, loadingError: null })

    // Re-load everything.
    Promise
      .all([
        loadUserLessons(),
        loadLessonStatuses(),
        loadLessonTypes(),
      ])
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

  onRefreshListClicked() {
    this.loadData()
  }

  onOpenCreateClicked() {
    this.setState({
      lessonCreationError: null,
      lessonCreationDialogVisible: true,
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

  onCreateLessonClicked() {
    this.setState({
      lessonCreationError:      null,
      lessonCreationInProgress: true,
    })

    const formData = this.refs.creationForm.getFormData()
    this.props.actions.createLesson(formData)
      .then(resultingAction => {
        if (resultingAction.error) {
          this.setState({
            lessonCreationError:      resultingAction.payload,
            lessonCreationInProgress: false,
          })
        } else {
          this.setState({
            lessonCreationInProgress:     false,
            lessonCreationDialogVisible:  false,
          })
        }
      })
  }

  onCancelLessonClicked() {
    this.setState({ lessonCreationDialogVisible: false })
  }

  onStatusFilterIdChanged(e, i, value) {
    this.setState({ statusFilterId: value })
  }

  onTypeFilterIdChanged(e, i, value) {
    this.setState({ typeFilterId: value })
  }


  render() {
    const { lessons, statuses, types } = this.props
    const {
      gridVisible,
      loadingError,
      isDataLoading,
      statusFilterId,
      typeFilterId
    } = this.state

    const filteredLessons = lessons
      .filter(lesson => {
        if (statusFilterId !== null) {
          return lesson.lessonStatus.id === statusFilterId
        }

        return true
      })
      .filter(lesson => {
        if (typeFilterId !== null) {
          return lesson.type.id === typeFilterId
        }

        return true
      })

    let content = null
    if (loadingError) {
      content = <ListError error={loadingError} />
    } else if (filteredLessons.length < 1) {
      content = <ListEmpty />
    } else {
      content = <LessonTable lessons={filteredLessons} />
    }

    const statusMenuItems = statuses
      .map(status => (
        <MenuItem
          key={status.id}
          value={status.id}
          primaryText={status.name} />
      ))
    const typeMenuItems = types
      .map(type => (
        <MenuItem
          key={type.id}
          value={type.id}
          primaryText={type.name} />
      ))
    const allFilterMenuItem = (
      <MenuItem
        key="all"
        value={null}
        primaryText="All" />
    )

    statusMenuItems.unshift(
      <MenuItem
        key="all"
        value={null}
        primaryText="All Statuses" />
    )
    typeMenuItems.unshift(
      <MenuItem
        key="all"
        value={null}
        primaryText="All Types" />
    )
    return (
      <div className={style.main}>
        <div className={style.filterBarWrapper}>
          <div className={classNames(style.filterBar, { [style.filterBar__wide]: !gridVisible })}>
            <FontIcon
              className="material-icons"
              color="#ffffff">filter_list</FontIcon>
            <SelectField
              value={statusFilterId}
              onChange={::this.onStatusFilterIdChanged}
              className={style.filterSelect}
              labelStyle={{ color: '#fff' }}>
              {statusMenuItems}
            </SelectField>
            <SelectField
              value={typeFilterId}
              onChange={::this.onTypeFilterIdChanged}
              className={style.filterSelect}
              labelStyle={{ color: '#fff' }}>
              {typeMenuItems}
            </SelectField>
          </div>
        </div>
        <div className={style.content}>{content}</div>
        <div className={style.buttons}>
          <ListButtons
            disabled={isDataLoading}
            onRefreshClicked={::this.onRefreshListClicked} />
        </div>
      </div>
    )
  }
}

export default Lessons
