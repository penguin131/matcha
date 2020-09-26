import React, {useEffect, useState} from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
import { Link } from 'react-router-dom'
import css from './ChatPage.module.less'
import { ws } from '../../services/backendUrl'
import * as services from '../../services/services.js'
import axios from 'axios'

const token = localStorage.token

const webSocket = new WebSocket(`${ws}${token}`)

const ChatPage = () => {
  const [friendsListIsLoading, setFriendsListIsLoading] = useState(false)
  const [chatListIsLoading, setChatListIsLoading] = useState(false)
  const [friendsList, setFriendsList] = useState([])
  const [currentChat, setCurrentChat] = useState(localStorage.currentChat)

  const [messages, setMessages] = useState([])
  
  useEffect(() => {
    webSocket.onopen = () => {
      console.log('connected')
    }
    webSocket.onclose = () => {
      console.log('closed')
    }
    
    return () => {
      webSocket.close()
      console.log('closed')
    }
  }, [])
  useEffect(() => {
    webSocket.onmessage = (message) => {
      setMessages([JSON.parse(message.data), ...messages])
    }
  }, [messages])

  const onSubmit = (values) => {
    const message = {
      type: 0,
      msgText: values.message,
      from: localStorage.currentUser,
      to: currentChat,
      date: Date.now(),
    }

    webSocket.send(JSON.stringify(message))
  }

  useEffect(() => {
    services.getAllFriends(setFriendsListIsLoading, setFriendsList)
  }, [])

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()
    services.fetchData(setChatListIsLoading, setMessages, 'getChatHistory', currentChat, source)
    return () => {
      source.cancel();
    };
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
          {currentChat && <Link to={`/profile/:${currentChat}`}>
            <span className={css.userName}>{currentChat}</span>
          </Link>}
          
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
