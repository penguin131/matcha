import React from 'react'
import { Form, Field } from 'react-final-form'
import css from './ChatForm.module.less'

const ChatForm = ( onSubmit ) => {
    return (
        <div className={css.chatFormContainer}>
            <Form
                onSubmit={e => onSubmit(e)}
                render={({handleSubmit, submitting, pristine}) => (
                    <form
                        onSubmit={handleSubmit}
                        className={css.chatForm}
                    >
                        <Field name='message'>
                            {({ input }) => (
                                <input {...input} className={css.text} placeholder='message...'/>  
                            )}
                        </Field>
                        <button type="submit" className={css.button} disabled={submitting || pristine}>
                            Send
                        </button>
                    </form>
                )}
            />
        </div>
    )
}

export default ChatForm
