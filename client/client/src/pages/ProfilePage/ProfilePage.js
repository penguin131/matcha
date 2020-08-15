import React, { useState, useEffect } from "react";
import Carousel from '../../components/Carousel/Carousel'
import * as services from '../../services/settings.js'
import { ip } from '../../services/backendUrl'
import css from './ProfilePage.module.less'

const ProfilePage = ({match}) => {
  const [userProfile, setUserProfile] = useState({})
  const [isLoading, setIsLoading] = useState(false)
  const [userPhotos, setUserPhotos] = useState([])
  const user = match.params.login.substring(1, match.params.login.length)

  useEffect(() => {
      services.getUserProfile(setIsLoading, setUserProfile, user)
      services.getUserPhotos(setIsLoading, setUserPhotos, user)
  }, [])

  const {
    first_name,
    last_name,
    login,
    biography
  } = userProfile

  return (
    <div className={css.mainSectionContainer}>
      <div className={css.userName}>{`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}</div>
        <Carousel photos={userPhotos}/>  
      <div className={css.userInfo}>{`${biography || '-'}`}</div>
    </div>
  );
}

export default ProfilePage
