import React from 'react'
import Loader from '../../Loader/Loader'
import css from './ConversationList.module.less'

const Conversation = ({ data, setCurrentChat, index }) => {
  const { login } = data

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
        </div>
      </div>
    </div>  
  )
}

const ConversationList = ({ data = [], setCurrentChat, isLoading }) => {
  
  return (
    <div className={css.conversationList}>
      {!isLoading ? (
        data.length > 0 ? data.map((element, i) => (
          <Conversation
            key={i}
            index={i}
            data={element}
            setCurrentChat={setCurrentChat}
          />
        )) : 'No chats'
      ) : <Loader/>}
    </div>
  )
}

export default ConversationList
