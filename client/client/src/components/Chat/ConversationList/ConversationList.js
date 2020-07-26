import React from 'react'
import moment from 'moment'
import Loader from '../../Loader/Loader'
import css from './ConversationList.module.less'

const Conversation = ({ data, setCurrentChat, index }) => {
  const { login, date, lastMessage } = data
  //const formatedDate = date/*  && moment(date).format('MMM Do') */
  
  const onClick = () => {
    setCurrentChat(login)
    localStorage.currentChat = login
  }

  return (
    <div className={`${css.conversation}`} onClick={onClick}>
      <div className={css.messageStatus}>+</div>
      <div className={css.conversationDetails}>
        <div className={css.conversationTitle}>
          <div className={css.titleText}>{login}</div>
         {/*  <div className={css.tetleDate}>{formatedDate}</div> */}
        </div>
  {/*       <div className={css.conversationMessage}>
          {lastMessage && lastMessage}
        </div> */}
      </div>
    </div>  
  )
}

const ConversationList = ({ data = [], setCurrentChat, isLoading }) => {

  return (
    <div className={css.conversationList}>
      {!isLoading ? (
        data.map((element, i) => (
          <Conversation
            key={i}
            index={i}
            data={element}
            setCurrentChat={setCurrentChat}
          />
        ))
      ) : <Loader/>}
    </div>
  )
}

export default ConversationList
