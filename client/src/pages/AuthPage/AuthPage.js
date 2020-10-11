import React, { useEffect } from 'react'
import JoinForm from '../../components/forms/JoinForm/JoinForm'
import LoginForm from '../../components/forms/LoginForm/LoginForm'
import { Switch, Link, Route } from 'react-router-dom'
import * as services from '../../services/services'
import css from './AuthPage.module.less'
import Button from '../../components/Button/Button'

const LoginButton = () => {
	return (
		<Link to='/login'>
			<Button label='Log in' className={css.loginButton}/>
		</Link>
	)
}

const JoinButton = () => {
	return (
		<Link to='/join' >
			<Button label='Join us' className={css.loginButton}/>
		</Link>
	)
}

const AuthPage = ({ setIsAuth }) => {
	useEffect(() => {
		localStorage.removeItem('key')
	}, [])
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
										<LoginForm	onSubmit={services.login}
																setIsAuth={setIsAuth}/>
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
