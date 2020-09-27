import React from 'react'
import moment from 'moment'
import css from './ChatList.module.less'
import Loader from '../../Loader/Loader'

const MessageRow = ({ msgText, date, myself }) => { 
  const formatedDate = date && moment(date).format('MMM Do')
  const whoseMessage = myself ? `${css.messageRow} ${css.youMessage}`
    : `${css.messageRow} ${css.otherMessage}`
  
  return (
    <div className={whoseMessage}>
      <div className={css.messageDetails}>
        <div className={css.messageText}>{msgText}</div>
        <div className={css.messageDate}>{formatedDate}</div>
      </div>
    </div>
  )
}

const ChatList = ({ currentChat, messages = [], isLoading }) => {
  return (
    <div className={css.chatContainer}>
      {currentChat ? isLoading ? <Loader/> :
      <div className={css.chatList}>
        {messages.length && messages.map((element, i) => {
          const { msgText, from, date } = element

          return (
            <MessageRow
              key={i}
              msgText={msgText}
              date={date}
              myself={from !== currentChat}
            />
          )
        })}
      </div> : <p>select chat...</p>}
    </div>
  )
}

export default ChatList
