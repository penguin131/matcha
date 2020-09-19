import React, { useState } from 'react'
import { Form, Field } from 'react-final-form'
import Loader from '../../Loader/Loader'
import css from '../authForms.module.less'
import settingsCss from './SettingsForm.module.less'

const SettingsForm = ({ onSubmit, data, isLoading, geolocation }) => {
  const [isFormLoading, setFormIsLoading] = useState(false)
  const {
    biography,
    /* sex,
    sex_preferences, */
  } = data

  const sex = 'male'
  const sex_preferences = 'female'

  console.log(data)

  return (
    <div className={css.authFormContainer}>{

        <>
          <Form
            onSubmit={e => onSubmit(e, setFormIsLoading)}
            mutators={{
              setLoading: (args, state, utils) => {
                utils.changeValue(state, 'isLoading', () => args[0])
              }
            }}
            initialValues={{
              isLoading: false,
              biography: biography,
              sex: sex,
              sex_preferences: sex_preferences,
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
                errors.oldPassword = 'Required'
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
              <Field name="biography">
                {({ input, meta }) => (
                  <div className={settingsCss.settingsTextarea}>
                    <textarea {...input} type="text" placeholder="Biography"/>
                    {meta.error && meta.touched && <span>{meta.error}</span>}
                  </div>
                )}
              </Field>
              <div>Sex:</div>
              <div>
                <Field
                  name="sex"
                  component="input"
                  type="radio"
                  value="male"
                /> male
                <Field
                  name="sex"
                  component="input"
                  type="radio"
                  value="female"
                /> female
              </div>
              <div>Sex preferences:</div>
              <div>
                <Field
                  name="sex_preferences"
                  component="input"
                  type="radio"
                  value="male"
                /> male
                <Field
                  name="sex_preferences"
                  component="input"
                  type="radio"
                  value="female"
                /> female
              </div>
             {/*  <Field name="username">
                {({ input, meta }) => (
                  <div className={css.fieldString}>
                    <input {...input} type="text" placeholder="Username" />
                    {meta.error && meta.touched && <span>{meta.error}</span>}
                  </div>
                )}
              </Field> */}
              {/* <Field name="email">
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
              </Field>  */}
              <div className={css.buttons}>
                <button className={css.submitButton} type="submit" disabled={submitting || pristine}>
                  Save
                </button>
              </div>
              {isFormLoading && <div><Loader/></div>}
            </form>
          )}/>
        </>
      }   
    </div>     
  )
}

export default SettingsForm
