import React from 'react'
import JoinForm from '../../components/forms/JoinForm/JoinForm'
import LoginForm from '../../components/forms/LoginForm/LoginForm'
import { Switch, Link, Route } from 'react-router-dom'
import * as services from '../../services/services'
import css from './AuthPage.module.less'

const LoginButton = () => {
	return (
		<Link to='/login'>
			<button className={css.loginButton}>
				Log in
			</button>
		</Link>
	)
}

const JoinButton = () => {
	return (
		<Link to='/join'>
			<button className={css.loginButton}>
				Join us
			</button>
		</Link>
	)
}

const AuthPage = ({ setIsAuth }) => {
	return (
		<>
			<Switch>
				<Route	path='/login'
								component={JoinButton}/>
				<Route	path='/join'
								component={LoginButton}/>
			</Switch>
			<div className={css.joinPageContainer}>
				<Switch>
					<Route	path='/login'
									component={() => (
										<LoginForm onSubmit={services.login} setIsAuth={setIsAuth}/>
									)}/>
					<Route	path='/join'
									component={() => (
										<JoinForm onSubmit={services.join}/>
									)}/>
				</Switch>
			</div>
		</>
	)
}

export default AuthPage
