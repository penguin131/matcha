import React, {useState, useEffect } from 'react'
import css from './ChatList.module.less'
import Loader from '../../Loader/Loader'
import * as services from '../../../services/chat'

const MessageRow = ({ data, myself }) => {
    return (
        <div className={myself? `${css.messageRow} ${css.youMessage}` : `${css.messageRow} ${css.otherMessage}`}>
            <div className={css.messageDetails}>
                <div className={css.messageText}>1ds;asdasffafkmvsdlk23</div>
                <div className={css.messageDate}>123</div>
            </div>
        </div>
    )
}

const ChatList = ({ currentChat, messages }) => {
    const [isLoading, setIsLoading] = useState(false)

    /* useEffect(() =>{
        services.getUser(setIsLoading, setMessages, currentChat)
    }) */
    console.log(currentChat)
    return (
        <div className={css.chatList}>
            {currentChat ? isLoading ? <Loader/> :
            messages.map((element, i) => (
                <MessageRow key={i} data={element} myself/>
            )) : <p>select chat...</p>}
        </div>
    )
}

export default ChatList
