import React from 'react'
import { Form, Field } from 'react-final-form'
import css from '../authForms.module.less'
import Loader from '../../Loader/Loader'

const ChangeEmailForm = ({ onSubmit, isFormLoading, url }) => {  
  return (
    <div className={css.authFormContainer}>
      <Form
        onSubmit={e => {
          const data = {
            "new_email": e.newEmail,
            "password": e.password
          }
          onSubmit(url, data)
        }}
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
          if (!values.newEmail) {
            errors.newEmail = 'Required'
          }
          if (!values.password) {
            errors.password = 'Required'
          }
          return errors
        }}
        render={({ handleSubmit, form, submitting, pristine, values }) => (
          <form onSubmit={handleSubmit} className={css.finalForm}>
            <Field name="newEmail">
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
            <div className={css.loaderBlock}>
                {isFormLoading && <Loader/>}
            </div>
            <div className={css.buttons}>
              <button className={css.submitButton} type="submit" disabled={submitting || pristine}>
                Change email
              </button>
            </div>
          </form>
        )}
      />
    </div>     
  )
}

export default ChangeEmailForm
