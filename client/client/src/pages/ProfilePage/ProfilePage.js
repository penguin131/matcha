import React, { useState, useEffect } from "react";
import Carousel from '../../components/Carousel/Carousel'
import * as services from '../../services/services.js'
import css from './ProfilePage.module.less'
import {ReactComponent as LikeLogo} from '../../imgs/like.svg'
import {ReactComponent as DislikeLogo} from '../../imgs/dislike.svg'
import axios from 'axios'

const ProfilePage = ({match}) => {
  const [userProfile, setUserProfile] = useState({})
  const [isLoading, setIsLoading] = useState(false)
  const [userPhotos, setUserPhotos] = useState([])
  const user = match.params.login.substring(1, match.params.login.length)

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    Promise.all([
      services.fetchData(setIsLoading, setUserProfile, 'getUserProfileForLogin', user, source),
      services.fetchData(setIsLoading, setUserPhotos, 'getUserPhotos', user, source)
    ])
    return () => {
      source.cancel();
    };
  }, [user])

  const {first_name, last_name, login, biography, rating, sex} = userProfile

  return (
    <div className={css.mainSectionContainer}>
      <div className={css.userName}>{`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}</div>
      <div>rating: {rating}</div>
      <div>sex: {sex}</div>
      {/* <Carousel photos={userPhotos}/>   */}
      <div className={css.userInfo}>{`${biography || '-'}`}</div>
      <div className={css.likePanel}>
        <div  className={`${css.likeElement} ${css.like}`}
              onClick={() => services.setLikeDislike(user, 'setLike')}><LikeLogo/></div>
        <div  className={`${css.likeElement} ${css.dislike}`}
              onClick={() => services.setLikeDislike(user, 'setComplaint')}><DislikeLogo/></div>
      </div>
    </div>
  );
}

export default ProfilePage
