import React, { useEffect } from 'react'
import Loader from '../../components/Loader/Loader'
import ProfileCard from '../../components/ProfileCard/ProfileCard'
import {useGetAxiosFetch} from '../../services/useAxiosFetch'
import {likedUsersUrl} from '../../services/services'
import css from './LikePage.module.less'

const token = localStorage.token

const LikePage = () => {
  const config = {headers: {'Authorization': token}}
  const [{data, loading}, sendGetRequest] = useGetAxiosFetch(config)

  useEffect(() => {
    sendGetRequest(likedUsersUrl)
  }, [sendGetRequest])
 
  return (
    <div className={css.mainSectionContainer}>
      {loading ?
        <div><Loader/></div> :
        data?.data?.map((profile, i) => (
          <ProfileCard  key={i}
                        user={profile.login}
                        userProfile={profile}/>
        ))}
    </div>
  )
}

export default LikePage
