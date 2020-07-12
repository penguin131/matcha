import React, {useEffect, useState, useContext} from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
import { ReactComponent as TrashLogo } from '../../imgs/trash.svg'
import css from './ChatPage.module.less'
import * as services from '../../services/chat.js'
import { AuthContext } from '../../components/context/AuthContext'

const ChatPage = () => {
    const [isLoading, setIsLoading] = useState(false)
    const [friendsList, setFriendsList] = useState([])
    const [currentChat, setCurrentChat] = useState(localStorage.currentChat)
    const { messages, setMessages, webSocket } = useContext(AuthContext)

    const onSubmit = (values) => {
        webSocket.current.send(values.message)
        setMessages([...messages, values.message])
    }

    useEffect(() => {
        services.getAllFriends(setIsLoading, setFriendsList)
    }, [])
 
    return (
        <div className={css.chatContainer}>
            <div className={css.chatBar}>
                <ConversationList
                    data={friendsList}
                    setCurrentChat={setCurrentChat}
                />
            </div>
            <div className={css.chatWindow}>
                <div className={css.chatTitle}>
                    <span className={css.userName}>{currentChat && currentChat}</span>
                    <TrashLogo className={css.trashLogo}/>
                </div>
                <ChatList currentChat={currentChat} messages={messages} setMessages={setMessages}/>
                <ChatForm onSubmit={onSubmit}/>
            </div>
        </div>
    )
}

export default ChatPage
