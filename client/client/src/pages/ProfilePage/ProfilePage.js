import React, { useEffect } from "react";
import css from './ProfilePage.module.less'
import "react-image-gallery/styles/css/image-gallery.css";
import ProfileCard from '../../components/ProfileCard/ProfileCard'
import { useGetAxiosFetch } from '../../services/useAxiosFetch'
import { userProfileUrl, userPhotosUrl } from '../../services/services'

const token = localStorage.token

const ProfilePage = ({match}) => {
  const config = {headers: {'Authorization': token}}
  const [userProfile, fetchUserProfile] = useGetAxiosFetch(config)
  const [userPhotos, fetchUserPhotos] = useGetAxiosFetch(config)
  const user = match.params.login.substring(1, match.params.login.length)
  
  useEffect(() => {
    Promise.all([
      fetchUserProfile(`${userProfileUrl}/${user}`),
      fetchUserPhotos(`${userPhotosUrl}/${user}`)
    ])
  }, [fetchUserProfile, fetchUserPhotos, user])

  return (
    <div className={css.mainSectionContainer}>
      <ProfileCard  user={user}
                    userProfile={userProfile.data?.data}
                    userPhotos={userPhotos.data?.data}
                    profileIsLoading={userProfile.loading}
                    imagesIsLoading={userPhotos.loading}/>
    </div>
    
  );
}

export default ProfilePage
