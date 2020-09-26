import React, { useState, useEffect } from "react";
import * as services from '../../services/services.js'
import css from './ProfilePage.module.less'
import {ReactComponent as LikeLogo} from '../../imgs/like.svg'
import {ReactComponent as DislikeLogo} from '../../imgs/dislike.svg'
import ImageGallery from 'react-image-gallery';
import axios from 'axios'
import Loader from '../../components/Loader/Loader'
import "react-image-gallery/styles/css/image-gallery.css";

const ProfilePage = ({match}) => {
  const [userProfile, setUserProfile] = useState({})
  const [profileIsLoading, setProfileIsLoading] = useState(false)
  const [imagesIsLoading, setImagesIsLoading] = useState(false)
  const [userPhotos, setUserPhotos] = useState([])
  const user = match.params.login.substring(1, match.params.login.length)
  const images = userPhotos.map(photo => ({original: photo.data, thumbnail: photo.data})) 

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    Promise.all([
      services.fetchData(setProfileIsLoading, setUserProfile, 'getUserProfileForLogin', user, source),
      services.fetchData(setImagesIsLoading, setUserPhotos, 'getUserPhotos', user, source)
    ])

    return () => {
      source.cancel();
    };
  }, [user])

  const {first_name, last_name, login, biography, rating, sex, has_like, has_dislike} = userProfile

  return (
    <div className={css.mainSectionContainer}>
      <div className={css.profileInfoContainer}>
        {profileIsLoading ? <div><Loader/></div> : <>
          <div className={css.userName}>{`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}</div>
          <div>rating: {rating}</div>
          <div>sex: {sex}</div>
          <div className={css.userInfo}>{`${biography || '-'}`}</div>
          <div className={css.likePanel}>
            <div  className={`${css.likeElement} ${has_like ? css.activeLike : css.like}`}
                  onClick={() => (
                    has_like ? () => {} : services.setLikeDislike(user, 'setLike'))
                  }><LikeLogo/></div>
            <div  className={`${css.likeElement} ${has_dislike ? css.activeDislike : css.dislike}`}
                  onClick={() => (
                    has_dislike ? () => {} : services.setLikeDislike(user, 'setComplaint')
                  )}><DislikeLogo/></div>
          </div>
        </> }
      </div>
      <div className={css.galleryContainer}>
        {imagesIsLoading ? <div><Loader/></div> : images.length > 0 ? 
          <ImageGallery items={images} /> : null}
       </div>
    </div>
  );
}

export default ProfilePage
