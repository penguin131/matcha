import React from 'react'
import { Form, Field } from 'react-final-form'
import Loader from '../../Loader/Loader'
import Button from '../../../components/Button/Button'
import css from './SettingsForm.module.less'

const NumberInput = ({ input, meta, ...rest }) => {
  return (
    <input {...input} {...rest} type="number" min='0'/>
  )
}

const SettingsForm = ({isFormLoading, onSubmit, data, url}) => {
  const { biography, sex, sex_preferences} = data

  return (
    <div className={css.authFormContainer}>{
      <>
        <Form
          onSubmit={e => {
            const data = {
              'sex': e.sex,
              'biography': e.biography,
              'sex_preferences': e.sexPreferences,
            }

            if (e.customGeolocation) {
              localStorage.setItem('customGeolocation', true)
              localStorage.setItem('customLongitude', e.longitude)
              localStorage.setItem('customLatitude', e.latitude)
            } else {
              localStorage.removeItem('customGeolocation')
              localStorage.removeItem('customLongitude')
              localStorage.removeItem('customLatitude')
            }
            onSubmit(url, data)
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
            customGeolocation: localStorage.getItem('customGeolocation') || false,
            longitude: localStorage.getItem('customLongitude'),
            latitude: localStorage.getItem('customLatitude'),
          }}
          
          render={({handleSubmit, submitting, pristine, values }) => (
            <form onSubmit={handleSubmit} className={css.finalForm}>
              <Field name='biography'>
                {({ input, meta }) => (
                  <div className={css.settingsTextarea}>
                    <textarea {...input} type='text' placeholder='Biography'/>
                    {meta.error && meta.touched && <span>{meta.error}</span>}
                  </div>
                )}
              </Field>
              <div className={css.inputsBlock}>
                Sex:
                <Field  name='sex'
                        component='input'
                        type='radio'
                        value='male'/> male
                <Field  name='sex'
                        component='input'
                        type='radio'
                        value='female'/> female
              </div>
              <div className={css.inputsBlock}>
                Sex preferences:
                <Field  name='sexPreferences'
                        component='input'
                        type='radio'
                        value='male'/> male
                <Field  name='sexPreferences'
                        component='input'
                        type='radio'
                        value='female'/> female
              </div>

              <div className={css.inputsBlock}>
                <div>
                  Custom geolocation: <Field  name='customGeolocation'
                                              component='input'
                                              type='checkbox'/>
                </div>
                <div>
                  Longitude:
                  <Field  name='longitude'
                          component={NumberInput}/>
                </div>
                <div>
                  Latitude:
                  <Field  name='latitude'
                          component={NumberInput}/>
                </div>
              </div>
            <div className={css.buttonsBlock}>
              <Button label='Save' className={css.submitButton} type='submit' disabled={submitting || pristine}/>
            </div>
            {isFormLoading && <div><Loader/></div>}
          </form>
        )}/>
      </>}   
    </div>     
  )
}

export default SettingsForm
