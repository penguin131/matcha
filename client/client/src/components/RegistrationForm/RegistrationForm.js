import React from 'react'
import { Form, Field } from 'react-final-form'
import css from './RegistrationForm.module.less'

const onSubmit = async values => {
  window.alert(JSON.stringify(values, 0, 2))
}

const RegistrationForm = () => {
  return (
    <div className={css.registrationFormContainer}>
      <Form
        onSubmit={onSubmit}
        validate={values => {
          const errors = {}
          if (!values.username) {
            errors.username = 'Required'
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
            <div className="buttons">
              <button type="submit" disabled={submitting || pristine}>
              Submit
              </button>
            </div>
          </form>
        )}
      />
    </div>     
  )
}

export default RegistrationForm
