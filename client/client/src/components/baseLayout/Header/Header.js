import React from 'react'
import { ReactComponent as Logo } from '../../../imgs/logo.svg';
import { ReactComponent as Logout } from '../../../imgs/logout.svg';
import defaultAva from '../../../imgs/defaultAva.png'
import { Link } from 'react-router-dom'
import Loader from '../../../components/Loader/Loader'
import css from './Header.module.less'
import Notification from '../../Notifications/Notifications'

const Header = ({ userProfile, isLoading, setIsAuth, notification, avatar }) => {
	const logout = () => {
		setIsAuth(false)
		localStorage.removeItem('currentUser')
		localStorage.removeItem('token')
		localStorage.removeItem('currentChat')
	}

	return (
		<header className={css.headerContainer}>
			<div className={css.logo}>
				<Link to='/'><Logo/></Link>
			</div>
			<Notification data={notification}/>
			<div className={css.userInfo}>
				{isLoading ? <div className={css.loaderBlock}><Loader/></div> : <>
					<div>
						<div>{userProfile?.login || '-'}</div>
						<div className={css.rating}>rating: {userProfile?.rating || '-'}</div>
					</div>
					<div >
						<Link to='/settings'>
							<img	className={css.userAva}
										alt=""
										src={avatar ? avatar.data : defaultAva}
							/>
						</Link>
					</div>
				</>}
				<div onClick={logout} className={css.logout}><Logout/></div>			
			</div>
		</header>
	)
}

export default Header
