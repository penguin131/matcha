import React from 'react'
import { Form, Field } from 'react-final-form'
import css from './RegistrationForm.module.css'


const sleep = ms => new Promise(resolve => setTimeout(resolve, ms))

const onSubmit = async values => {
  await sleep(300)
  window.alert(JSON.stringify(values, 0, 2))
}

const RegistrationForm = () => {
  return (
    <div className={css.registrationFormContainer}>
      <Form
        onSubmit={onSubmit}
        initialValues={{ stooge: 'larry', employed: false }}
        render={({ handleSubmit, form, submitting, pristine, values }) => (
          <form onSubmit={handleSubmit}>
          <div>
              <label>First Name</label>
              <Field
              name="firstName"
              component="input"
              type="text"
              placeholder="First Name"
              />
          </div>
          <div>
              <label>Last Name</label>
              <Field
              name="lastName"
              component="input"
              type="text"
              placeholder="Last Name"
              />
          </div>
          <div>
              <label>Email</label>
              <Field
              name="email"
              component="input"
              type="text"
              placeholder="Email"
              />
          </div>
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
