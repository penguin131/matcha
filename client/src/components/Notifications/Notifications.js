import React from 'react'
import { Link } from 'react-router-dom'
import css from './Notifications.module.less'

const Notifications = ({ data }) => {
  let notification = null

  if (data?.type === 'chat_message') {
    notification = <span>
      New message from <Link to={`/profile/:${data.from}`}>{data.from}</Link>
    </span>
  }

  if (data?.type === 'notification') {
    notification = <span>
      <Link to={`/profile/:${data.from}`}>{data.from}</Link> {data.msgText}
    </span>
  }

  return (
    notification ? <div className={css.notification}>{notification}</div> : null
  )
}

export default Notifications
