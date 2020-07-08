import React from 'react'
import ChatForm from '../../components/forms/ChatForm/ChatForm'
import { ReactComponent as TrashLogo } from '../../imgs/trash.svg'
import css from './ChatPage.module.less'

const onSubmit = (values) => {
    console.log(values)
}
const ChatPage = () => {
    return (
        <div className={css.chatContainer}>
            <div className={css.chatBar}>ChatBar</div>
            <div className={css.chatWindow}>
                <div className={css.chatTitle}>
                    <span className={css.userName}>Kekeke kekek</span>
                    <TrashLogo className={css.trashLogo}/>
                </div>
                <div className={css.chatList}>ChatList</div>
                <ChatForm onSubmit={onSubmit}/>
            </div>
        </div>
    )
}

export default ChatPage
