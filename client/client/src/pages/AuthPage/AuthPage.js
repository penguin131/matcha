import React from 'react'
import JoinPage from '../../components/forms/JoinForm/JoinForm'
import LoginPage from '../../components/forms/LoginForm/LoginForm'
import { Switch, Link, Route } from 'react-router-dom'
import css from './AuthPage.module.less'

const LoginButton = () => {
    return (
        <Link to='/login'><button className={css.loginButton}>Log in</button></Link>
    )
}

const JoinButton = () => {
    return (
        <Link to='/join'><button className={css.loginButton}>Join us</button></Link>
    )
}

const AuthPage = () => {

    return (
        <>
            <Switch>
                <Route path='/login' component={JoinButton}/>
                <Route path='/join' component={LoginButton}/>
            </Switch>
        
            <div className={css.joinPageContainer}>
                <Switch>
                    <Route path='/login' component={LoginPage}/>
                    <Route path='/join' component={JoinPage}/>
                </Switch>
            </div>
        </>
    )
}

export default AuthPage
