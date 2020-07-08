import React from 'react'
import ConversationList from '../../components/Chat/ConversationList/ConversationList'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import ChatList from '../../components/Chat/ChatList/ChatList'
import { ReactComponent as TrashLogo } from '../../imgs/trash.svg'
import css from './ChatPage.module.less'

const onSubmit = (values) => {
    console.log(values)
}
const ChatPage = () => {
    return (
        <div className={css.chatContainer}>
            <div className={css.chatBar}>
                <ConversationList/>
            </div>
            <div className={css.chatWindow}>
                <div className={css.chatTitle}>
                    <span className={css.userName}>Kekeke kekek</span>
                    <TrashLogo className={css.trashLogo}/>
                </div>
                <ChatList/>
                <ChatForm onSubmit={onSubmit}/>
            </div>
        </div>
    )
}

export default ChatPage
