import React, { useState } from 'react'
import { Form, Field } from 'react-final-form'
import css from '../authForms.module.less'
import Loader from '../../Loader/Loader'
import { useHistory } from 'react-router-dom'

const LoginForm = ({ onSubmit, setIsAuth }) => {
  const [isLoading, setIsLoading] = useState(false)
  let history = useHistory()

  return (
    <div className={css.authFormContainer}>
      <Form
        onSubmit={e => onSubmit(e, setIsLoading, history, setIsAuth)}
        validate={values => {
          const errors = {}
          if (!values.username) {
            errors.username = 'Required'
          }
          if (!values.password) {
            errors.password = 'Required'
          }

          return errors
        }}
        render={({ handleSubmit, form, submitting, pristine, values }) => (
          <form onSubmit={handleSubmit} className={css.finalForm}>
            <Field name="username">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="text" placeholder="Username" />
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field>
            <Field name="password">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="password" placeholder="Password" />
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field>
            <div className={css.loaderBlock}>
                {isLoading && <Loader/>}
            </div>
            <div className={css.buttons}>
              <button className={css.submitButton} type="submit" disabled={submitting || pristine}>
                Log in
              </button>
            </div>
          </form>
        )}
      />
    </div> 
  )
}

export default LoginForm