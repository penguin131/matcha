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
  const [profileIsLoading, setProfileIsLiading] = useState(false)
  const [imagesIsLoading, setImagesIsLiading] = useState(false)
  const [userPhotos, setUserPhotos] = useState([])
  const user = match.params.login.substring(1, match.params.login.length)
   
const images = [
  {
    original: 'https://picsum.photos/id/1018/1000/600/',
    thumbnail: 'https://picsum.photos/id/1018/250/150/',
  },
  {
    original: 'https://picsum.photos/id/1015/1000/600/',
    thumbnail: 'https://picsum.photos/id/1015/250/150/',
  },
  {
    original: 'https://picsum.photos/id/1019/1000/600/',
    thumbnail: 'https://picsum.photos/id/1019/250/150/',
  },
];
  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    Promise.all([
      services.fetchData(setProfileIsLiading, setUserProfile, 'getUserProfileForLogin', user, source),
      services.fetchData(setImagesIsLiading, setUserPhotos, 'getUserPhotos', user, source)
    ])
    return () => {
      source.cancel();
    };
  }, [user])

  const {first_name, last_name, login, biography, rating, sex} = userProfile

  return (
    <div className={css.mainSectionContainer}>
      <div className={css.profileInfoContainer}>
        {profileIsLoading ? <div><Loader/></div> : <>
          <div className={css.userName}>{`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}</div>
          <div>rating: {rating}</div>
          <div>sex: {sex}</div>
          <div className={css.userInfo}>{`${biography || '-'}`}</div>
          <div className={css.likePanel}>
            <div  className={`${css.likeElement} ${css.like}`}
                  onClick={() => services.setLikeDislike(user, 'setLike')}><LikeLogo/></div>
            <div  className={`${css.likeElement} ${css.dislike}`}
                  onClick={() => services.setLikeDislike(user, 'setComplaint')}><DislikeLogo/></div>
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
