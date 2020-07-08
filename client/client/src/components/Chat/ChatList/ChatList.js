import React from 'react'
import css from './ChatList.module.less'

const MessageRow = ({ myself }) => {
    return (
        <div className={myself? `${css.messageRow} ${css.youMessage}` : `${css.messageRow} ${css.otherMessage}`}>
            <div className={css.messageDetails}>
                <div className={css.messageText}>1ds;asdasffafkmvsdlk23</div>
                <div className={css.messageDate}>123</div>
            </div>
        </div>
    )
}

const ChatList = () => {
    return (
        <div className={css.chatList}>
            <MessageRow myself/>
            <MessageRow/>
            <MessageRow/>
            <MessageRow/>
        </div>
    )
}

export default ChatList
