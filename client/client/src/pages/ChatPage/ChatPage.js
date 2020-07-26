import React, {useEffect, useState, useRef} from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
/* import { ReactComponent as TrashLogo } from '../../imgs/trash.svg' */
import css from './ChatPage.module.less'
import { ws } from '../../services/backendUrl'
import * as services from '../../services/chat.js'

const ChatPage = () => {
  const [friendsListIsLoading, setFriendsListIsLoading] = useState(false)
  const [chatListIsLoading, setChatListIsLoading] = useState(false)
  const [friendsList, setFriendsList] = useState([])
  const [currentChat, setCurrentChat] = useState(localStorage.currentChat)
  const token = localStorage.token
  const webSocket = useRef(new WebSocket(`${ws}${token}`))
  const [messages, setMessages] = useState([])
  
  useEffect(() => {
    webSocket.current.onopen = () => {
      console.log('connected')
    }
    webSocket.current.onclose = () => {
      console.log('closed')
    }
    
    return () => {
      webSocket.current.close()
      console.log('closed')
    }
  }, [])
useEffect(() => {
  webSocket.current.onmessage = (message) => {
    setMessages([JSON.parse(message.data), ...messages])
  }
}, [messages])
  const onSubmit = (values) => {
    const message = {
      type: 0,
      msgText: values.message,
      from: localStorage.currentUser,
      to: currentChat,
      date: new Date(),
    }

    webSocket.current.send(JSON.stringify(message))
    setMessages([message, ...messages])
   // services.getUserChatHistory(setChatListIsLoading, setMessages, currentChat)
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
