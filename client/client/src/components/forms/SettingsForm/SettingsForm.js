import React, { useState } from 'react'
import { Form, Field } from 'react-final-form'
import Loader from '../../Loader/Loader'
import css from '../authForms.module.less'
import settingsCss from './SettingsForm.module.less'

const SettingsForm = ({onSubmit, data }) => {
  const [isFormLoading, setFormIsLoading] = useState(false)
  const {
    biography,
    sex,
    sex_preferences,
  } = data

  return (
    <div className={css.authFormContainer}>{
      <>
        <Form
          onSubmit={e => {
            const data = {
              "sex": e.sex,
              "biography": e.biography,
              "sex_preferences": e.sexPreferences,
            }
            onSubmit(data, setFormIsLoading)
          }}
          mutators={{
            setLoading: (args, state, utils) => {
              utils.changeValue(state, 'isLoading', () => args[0])
            }
          }}
          initialValues={{
            isLoading: false,
            biography: biography,
            sex: sex,
            sexPreferences: sex_preferences,
          }}
          
        render={({handleSubmit, form, submitting, pristine, values }) => (
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
                name="sexPreferences"
                component="input"
                type="radio"
                value="male"
              /> male
              <Field
                name="sexPreferences"
                component="input"
                type="radio"
                value="female"
              /> female
            </div>
            <div className={css.buttons}>
              <button className={css.submitButton} type="submit" disabled={submitting || pristine}>
                  Save
              </button>
            </div>
            {isFormLoading && <div><Loader/></div>}
          </form>
        )}/>
      </>}   
    </div>     
  )
}

export default SettingsForm
