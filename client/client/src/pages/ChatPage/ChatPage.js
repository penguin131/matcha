import React, {useEffect, useState, useRef} from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
/* import { ReactComponent as TrashLogo } from '../../imgs/trash.svg' */
import css from './ChatPage.module.less'
import { ws, url } from '../../services/backendUrl'
import * as services from '../../services/chat.js'

const ChatPage = () => {
  const [friendsListIsLoading, setFriendsListIsLoading] = useState(false)
  const [chatListIsLoading, setChatListIsLoading] = useState(false)
  const [friendsList, setFriendsList] = useState([])
  const [currentChat, setCurrentChat] = useState(localStorage.currentChat)
  const token = localStorage.token
  const webSocket = useRef(new WebSocket(`${ws}/chat//${url}?token=${token}`))
  const [messages, setMessages] = useState([])
  
  useEffect(() => {
    webSocket.current.onopen = () => {
      console.log('connected')
    }
    webSocket.current.onclose = () => {
      console.log('closed')
    }
    webSocket.current.onmessage = (message) => {
      setMessages([message.data, ...messages])
    }
    return () => {
      webSocket.current.close()
      console.log('closed')
    }
  }, [])

  const onSubmit = (values) => {
    const message = {
      type: 0,
      msgText: values.message,
      from: localStorage.currentUser,
      to: currentChat,
    }

    webSocket.current.send(JSON.stringify(message))
    setMessages([ message, ...messages])
  }

  useEffect(() => {
    services.getAllFriends(setFriendsListIsLoading, setFriendsList)
  }, [])

  useEffect(() => {
    services.getUserChatHistory(setChatListIsLoading, setMessages, currentChat)
  }, [currentChat])


  return (
    <div className={css.chatContainer}>
      <div className={css.chatBar}>
        <ConversationList
          data={friendsList}
          setCurrentChat={setCurrentChat}
          isLoading={friendsListIsLoading}
        />
      </div>
      <div className={css.chatWindow}>
        <div className={css.chatTitle}>
          <span className={css.userName}>{currentChat && currentChat}</span>
          {/* <TrashLogo className={css.trashLogo}/> */}
        </div>
        <ChatList
          currentChat={currentChat}
          messages={messages}
          setMessages={setMessages}
          isLoading={chatListIsLoading}
        />
        <ChatForm onSubmit={onSubmit}/>
      </div>
    </div>
  )
}

export default ChatPage
