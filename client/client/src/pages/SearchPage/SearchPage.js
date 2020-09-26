import React, {useState, useEffect} from 'react'
import * as services from '../../services/services'
import ProfileCard from '../../components/ProfileCard/ProfileCard'
import css from './SearchPage.module.less'
import axios from 'axios'
import Button from '../../components/Button/Button'

const SearchPage = () => {
  const [userProfile, setUserProfile] = useState({})
  const [filters, setFilters] = useState({})
  const [userPhotos, setUserPhotos] = useState([])
  const [profileIsLoading, setProfileIsLoading] = useState(false)
  const [imagesIsLoading, setImagesIsLoading] = useState(false)

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    services.getNextUser(filters, setProfileIsLoading, setUserProfile)

    return () => {
      source.cancel();
    };
  }, [])

  useEffect(() => {
    const {login} = userProfile
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    services.fetchData(setImagesIsLoading, setUserPhotos, 'getUserPhotos', login, source)

    return () => {
      source.cancel();
    };
  }, [userProfile])

  return (
    <div className={css.searchContainer}>
      <ProfileCard  user={userProfile?.login}
                    userProfile={userProfile}
                    userPhotos={userPhotos}
                    profileIsLoading={profileIsLoading}
                    imagesIsLoading={imagesIsLoading}/>
      <Button onClick={() =>  services.getNextUser(filters, setProfileIsLoading, setUserProfile)} label="Next"/>
      
    </div>
  )
}

export default SearchPage
