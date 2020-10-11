import React, { useState } from 'react'
import { Form, Field } from 'react-final-form'
import css from '../authForms.module.less'
import Loader from '../../Loader/Loader'
import { useHistory } from 'react-router-dom'
import { redirect } from '../../../services/backendUrl'

const LoginForm = ({ onSubmit, setIsAuth }) => {
  const [isLoading, setIsLoading] = useState(false)
  let history = useHistory()
  const intraLogin = `https://api.intra.42.fr/oauth/authorize?client_id=7ff75fdfa415c5709f7d9257bc163dbd22654eae9a10799daffeb52026b924ac&redirect_uri=http%3A%2F%2F${redirect}&response_type=code`
  
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
            <Field name='username'>
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input  {...input}
                          type='text'
                          placeholder='Username'
                          autoComplete='off'/>
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field>
            <Field name='password'>
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input  {...input}
                          type='password'
                          placeholder='Password'
                          autoComplete='off'/>
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field>
            <div className={css.loaderBlock}>
                {isLoading && <Loader/>}
            </div>
            <div className={css.buttons}>
              <button className={css.submitButton} type='submit' disabled={submitting || pristine}>
                Log in
              </button>
              <a href={intraLogin}>
                <button className={css.submitButton} type='button'>
                  Log in with intra
                </button>
              </a>
            </div>
          </form>
        )}
      />
    </div> 
  )
}

export default LoginForm
