import React from 'react'
import { ReactComponent as Logo } from '../../../imgs/logo.svg';
import { ReactComponent as Logout } from '../../../imgs/logout.svg';
import defaultAva from '../../../imgs/defaultAva.png'
import {ip} from '../../../services/backendUrl'
import { Link } from 'react-router-dom'
import Loader from '../../../components/Loader/Loader'
import css from './Header.module.less'

const Header = ({ data, isLoading }) => {
  const { userProfile, userPhotos } = data
	const mainPhoto = userPhotos.length > 0 ? userPhotos.find(photo => photo.main) : null

	return (
		<header className={css.headerContainer}>
			<div className={css.logo}>
				<Link to='/'><Logo/></Link>
			</div>
			<div className={css.userInfo}>
				{isLoading ? <div><Loader/></div> : <>
					<div>
						<div>{userProfile?.login || '-'}</div>
						<div className={css.rating}>rating: {userProfile?.rating || '-'}</div>
					</div>
					<div >
						<Link to='/settings'>
							<img	className={css.userAva}
										alt=""
										src={mainPhoto ? mainPhoto.data : defaultAva}
							/>
						</Link>
					</div>
					<div><Logout/></div>
				</>}
				
			</div>
		</header>
	)
}

export default Header
