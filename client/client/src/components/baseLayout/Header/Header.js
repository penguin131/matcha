import React from 'react'
import { ReactComponent as Logo } from '../../../imgs/logo.svg';
import { ReactComponent as Logout } from '../../../imgs/logout.svg';
import defaultAva from '../../../imgs/defaultAva.png'
import {ip} from '../../../services/backendUrl'
import { Link } from 'react-router-dom'
import css from './Header.module.less'

const Header = ({ data }) => {
	const { userProfile, userPhotos } = data
	const mainPhoto = userPhotos.find(photo => photo.main)

	return (
		<header className={css.headerContainer}>
			<div className={css.logo}>
				<Link to='/'><Logo/></Link>
			</div>
			<div className={css.userInfo}>
				<div>
					<div>{userProfile?.login || '-'}</div>
					<div className={css.rating}>rating: {userProfile?.rating || '-'}</div>
				</div>
				<div >
					<Link to='/settings'>
						<img	className={css.userAva}
									alt=""
									src={mainPhoto?.imageId ? `http://${ip}/image/${mainPhoto?.imageId}` : defaultAva}
						/>
					</Link>
				</div>
				<div>	<Logout/></div>
			</div>
		</header>
	)
}

export default Header
