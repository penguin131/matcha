import React, { useState } from 'react'
import { Form, Field } from 'react-final-form'
import css from '../authForms.module.less'
import Loader from '../../Loader/Loader'

const JoinForm = ({ onSubmit }) => {
  const [isLoading, setIsLoading] = useState(false)
  
  return (
    <div className={css.authFormContainer}>
      <Form
        onSubmit={e => onSubmit(e, setIsLoading)}
        mutators={{
          setLoading: (args, state, utils) => {
            utils.changeValue(state, 'isLoading', () => args[0])
          }
        }}
        initialValues={{
          isLoading: false
        }}
        validate={values => {
          const errors = {}
          if (!values.username) {
            errors.username = 'Required'
          }
          if (!values.email) {
            errors.email = 'Required'
          }
          if (!values.password) {
            errors.password = 'Required'
          }
          if (!values.confirm) {
            errors.confirm = 'Required'
          } else if (values.confirm !== values.password) {
            errors.confirm = 'Must match'
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
            <Field name="email">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="email" placeholder="Email" />
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
            <Field name="confirm">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="password" placeholder="Confirm" />
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field> 
            <div className={css.loaderBlock}>
                {isLoading && <Loader/>}
            </div>
            <div className={css.buttons}>
            
              <button className={css.submitButton} type="submit" disabled={submitting || pristine}>
                Join us
              </button>
            </div>
          </form>
        )}
      />
    </div>     
  )
}

export default JoinForm
