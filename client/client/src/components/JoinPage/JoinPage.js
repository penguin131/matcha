import React from 'react'
import registrationForm from '../RegistrationForm/RegistrationForm'
import css from './JoinPage.module.css'
import RegistrationForm from '../RegistrationForm/RegistrationForm'

const JoinPage = () => {
  return (
    <>
      <button className={css.loginButton}>Log in</button>
      <div className={css.joinPageContainer}>
          <RegistrationForm/>
      </div>
    </>
  )
}

export default JoinPage
