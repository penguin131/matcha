import React from 'react'
import css from './ConversationList.module.less'

const Conversation = () => {
    return (
        <div className={`${css.conversation}`}>
            <div className={css.messageStatus}>+</div>
            <div className={css.conversationDetails}>
                <div className={css.conversationTitle}>
                    <div className={css.titleText}>keasasdasf af aseke
                    afasfasf
                    asfasffas</div>
                    <div className={css.tetleDate}>123</div>
                </div>
                <div className={css.conversationMessage}>
                    messывдмлсолтфирмыпфл таьвбдыошгрлмп
                </div>
            </div>
            
        </div>
        
    )
}

const ConversationList = () => {
    return (
        <div className={css.conversationList}>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>
            <Conversation/>

        </div>
    )
}

export default ConversationList
