import React, { useEffect } from 'react'
import Loader from '../../components/Loader/Loader'
import ProfileCard from '../../components/ProfileCard/ProfileCard'
import {useGetAxiosFetch} from '../../services/useAxiosFetch'
import {lookedUsersUrl} from '../../services/services'
import css from './WatchPage.module.less'

const token = localStorage.token

const WatchPage = () => {
  const config = {headers: {'Authorization': token}}
  const [{data, loading}, sendGetRequest] = useGetAxiosFetch(config)

  useEffect(() => {
    sendGetRequest(lookedUsersUrl)
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

export default WatchPage
