import React from 'react'
import { Link } from 'react-router-dom'
import css from './Notifications.module.less'

const Notifications = ({ data }) => {
  let notification = null

  if (data?.type === 'chat_message') {
    notification = <>
      New message from <Link to={`/profile/:${data.from}`}>{data.from}</Link>
    </>

  }

  if (data?.type === 'notification') {
    notification = <>
      <Link to={`/profile/:${data.from}`}>{data.from}</Link><div className={css.text}>{data.msgText}</div>
    </>
  }

  return (
    notification ? <div className={css.notification}>{notification}</div> : null
  )
}

export default Notifications
