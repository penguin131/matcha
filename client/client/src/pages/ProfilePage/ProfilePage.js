import React, { useState, useEffect } from "react";
import * as services from '../../services/services.js'
import axios from 'axios'
import css from './ProfilePage.module.less'
import "react-image-gallery/styles/css/image-gallery.css";
import ProfileCard from '../../components/ProfileCard/ProfileCard'

const ProfilePage = ({match}) => {
  const [userProfile, setUserProfile] = useState({})
  const [profileIsLoading, setProfileIsLoading] = useState(false)
  const [imagesIsLoading, setImagesIsLoading] = useState(false)
  const [userPhotos, setUserPhotos] = useState([])
  const user = match.params.login.substring(1, match.params.login.length)
  

  useEffect(() => {
    Promise.all([
      services.fetchData(setProfileIsLoading, setUserProfile, 'getUserProfileForLogin', user),
      services.fetchData(setImagesIsLoading, setUserPhotos, 'getUserPhotos', user)
    ])
  }, [user])

  return (
    <div className={css.mainSectionContainer}>
      <ProfileCard  user={user}
                    userProfile={userProfile}
                    userPhotos={userPhotos}
                    profileIsLoading={profileIsLoading}
                    imagesIsLoading={imagesIsLoading}/>
    </div>
    
  );
}

export default ProfilePage
