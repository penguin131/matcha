import React from 'react'
import Loader from '../../components/Loader/Loader'
import css from './ProfileCard.module.less'
import {ReactComponent as LikeLogo} from '../../imgs/like.svg'
import {ReactComponent as DislikeLogo} from '../../imgs/dislike.svg'
import ImageGallery from 'react-image-gallery';
import Chip from '../TagsInput/Chip/Chip'
import * as services from '../../services/services.js'

const ProfileCard = ({user, userProfile, userPhotos, profileIsLoading, imagesIsLoading}) => {
  const {first_name, last_name, login, biography, rating, sex, has_like, has_dislike, tags} = userProfile
  const images = userPhotos?.map(photo => ({original: photo.data, thumbnail: photo.data})) 

  return (
    <>
      <div className={css.profileInfoContainer}>
        {profileIsLoading ? <div><Loader/></div> : <>
          <div className={css.userName}>{`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}</div>
          <div>rating: {rating}</div>
          <div>sex: {sex}</div>
          <div className={css.userInfo}>{`${biography || '-'}`}</div>
          <div className={css.likePanel}>
            <div  className={`${css.likeElement} ${has_like ? css.activeLike : css.like}`}
                  onClick={() => ( services.setLikeDislike(user, 'setLike'))
                  }><LikeLogo/></div>
            <div  className={`${css.likeElement} ${has_dislike ? css.activeDislike : css.dislike}`}
                  onClick={() => ( services.setLikeDislike(user, 'setComplaint')
                  )}><DislikeLogo/></div>
          </div>
        </> }
      </div>
      <div>
      {tags?.map((tag, i) => (
          <Chip
            key={i}
            data={tag}
          />
        ))}
      </div>
      <div className={css.galleryContainer}>
        {imagesIsLoading ? <div><Loader/></div> : images.length > 0 ? 
          <ImageGallery items={images} /> : null}
       </div>
    </>
  );
}

export default ProfileCard
