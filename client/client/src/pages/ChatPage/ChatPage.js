import React, {useEffect, useState} from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
import { Link } from 'react-router-dom'
import css from './ChatPage.module.less'
import { useGetAxiosFetch } from '../../services/useAxiosFetch'
import { allFriendsUrl, chatHistoryUrl } from '../../services/services' 
import axios from 'axios'

const token = localStorage.token

const ChatPage = ({ webSocket }) => {
  const config = {headers: {'Authorization': token}}
  const [friendsList, fetchFriendsList] = useGetAxiosFetch(config)
  const [chatHistory, fetchChatHistory] = useGetAxiosFetch(config)
  const [currentChat, setCurrentChat] = useState(localStorage.currentChat)
  const [messages, setMessages] = useState([])

  useEffect(() => {
    fetchFriendsList(allFriendsUrl)
  }, [])
  
  useEffect(() => {
    webSocket.onmessage = (message) => {
      const data = JSON.parse(message.data)

      if (data.type === 'chat_message') {
        console.log(';e;')
        setMessages([JSON.parse(message.data), ...messages])
      }
    }
  }, [messages])

  useEffect(() => {
    fetchChatHistory(`${chatHistoryUrl}/${currentChat}`).then(res => res?.data && setMessages(res.data))
  }, [currentChat])

  const onSubmit = (values) => {
    const message = {
      type: 'chat_message',
      msgText: values.message,
      from: localStorage.currentUser,
      to: currentChat,
      date: Date.now(),
    }
    console.log(messages)
    setMessages([...messages, message])
    webSocket.send(JSON.stringify(message))
  }

  return (
    <div className={css.chatContainer}>
      <div className={css.chatBar}>
        <ConversationList
          data={friendsList.data?.data}
          setCurrentChat={setCurrentChat}
          isLoading={friendsList.loading}
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
          isLoading={false}
        />
        <ChatForm onSubmit={onSubmit}/>
      </div>
    </div>
  )
}

export default ChatPage
