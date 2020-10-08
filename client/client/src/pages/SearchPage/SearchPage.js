import React, {useState, useEffect} from 'react'
import {} from '../../services/services'
import ProfileCard from '../../components/ProfileCard/ProfileCard'
import css from './SearchPage.module.less'
import Button from '../../components/Button/Button'
import { useGetAxiosFetch, usePostAxiosFetch } from '../../services/useAxiosFetch'
import { nextUserProfileUrl, userPhotosUrl } from '../../services/services'

const SearchPage = () => {
  const token = localStorage.token
  const config = {headers: {'Authorization': token}}
  const [filters/* , setFilters */] = useState({})
  const [userProfile, fetchUserProfile] = usePostAxiosFetch(config)
  const [userPhotos, fetchUserPhotos] = useGetAxiosFetch(config)
  
  const fetchData = () => {
    fetchUserProfile(nextUserProfileUrl, filters).then((r) => {
      r?.data && fetchUserPhotos(`${userPhotosUrl}/${r.data.login}`)
    })
  }
  
  useEffect(() => {
    fetchData()
  }, [])

  return (
    <div className={css.searchContainer}>
      <ProfileCard  user={userProfile?.login}
                    userProfile={userProfile.data}
                    userPhotos={userPhotos.data}
                    profileIsLoading={userProfile.loading}
                    imagesIsLoading={userPhotos.loading}/>
      {userProfile.data && <Button onClick={() =>  fetchData()} label="Next"/>}
    </div>
  )
}

export default SearchPage