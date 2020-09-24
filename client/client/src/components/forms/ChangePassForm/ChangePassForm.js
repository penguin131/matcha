import React, { useState, useContext } from 'react'
import { Form, Field } from 'react-final-form'
import css from '../authForms.module.less'
import Loader from '../../Loader/Loader'
import { useHistory } from 'react-router-dom'

const ChangePassForm = ({ onSubmit }) => {
  const [isLoading, setIsLoading] = useState(false)
  
  return (
    <div className={css.authFormContainer}>
      <Form
        onSubmit={e => {
          const data = {
            "old_password": e.oldPassword,
            "new_password": e.newPassword,
          }
          onSubmit(data, setIsLoading)
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
          if (!values.oldPassword) {
            errors.oldPassword = 'Required'
          } 
          if (!values.newPassword) {
            errors.newPassword = 'Required'
          }
          if (!values.confirm) {
            errors.confirm = 'Required'
          } else if (values.confirm !== values.newPassword) {
            errors.confirm = 'Must match'
          } else if (values.confirm !== values.newPassword) {
            errors.confirm = 'Must match'
          }
          return errors
        }}
        render={({ handleSubmit, form, submitting, pristine, values }) => (
          <form onSubmit={handleSubmit} className={css.finalForm}>
            <Field name="oldPassword">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="password" placeholder="Old password" />
                  {meta.error && meta.touched && <span>{meta.error}</span>}
                </div>
              )}
            </Field>
            <Field name="newPassword">
              {({ input, meta }) => (
                <div className={css.fieldString}>
                  <input {...input} type="password" placeholder="New password" />
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
                Change password
              </button>
            </div>
          </form>
        )}
      />
    </div>     
  )
}

export default ChangePassForm
