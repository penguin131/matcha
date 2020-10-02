import React from 'react'
import Loader from '../../components/Loader/Loader'
import css from './ProfileCard.module.less'
import {ReactComponent as LikeLogo} from '../../imgs/like.svg'
import {ReactComponent as DislikeLogo} from '../../imgs/dislike.svg'
import ImageGallery from 'react-image-gallery';
import Chip from '../TagsInput/Chip/Chip'
import {setLikeUrl, setDislikeUrl} from '../../services/services'
import {useGetAxiosFetch} from '../../services/useAxiosFetch'

const token = localStorage.token

const ProfileCard = ({user, userProfile, userPhotos, profileIsLoading, imagesIsLoading}) => {
  const config = {headers: {'Authorization': token}}
  const [, sendGetRequest] = useGetAxiosFetch(config)
  console.log(userPhotos)
  const images = userPhotos?.data?.map(photo => ({original: photo.data, thumbnail: photo.data})) 

  return (
    <>
      <div className={css.profileInfoContainer}>
        {profileIsLoading ? <div><Loader/></div> : <>
          <div className={css.userName}>{`${userProfile?.first_name || '-'} ${userProfile?.login || '-'} ${userProfile?.last_name || '-'}`}</div>
          <div>rating: {userProfile?.rating}</div>
          <div>sex: {userProfile?.sex}</div>
          <div className={css.userInfo}>{`${userProfile?.biography || '-'}`}</div>
          <div className={css.likePanel}>
            <div  className={`${css.likeElement} ${userProfile?.has_like ? css.activeLike : css.like}`}
                  onClick={() => (sendGetRequest(`${setLikeUrl}/${user}`))
                  }><LikeLogo/></div>
            <div  className={`${css.likeElement} ${userProfile?.has_dislike ? css.activeDislike : css.dislike}`}
                  onClick={() => (sendGetRequest(`${setDislikeUrl}/${user}`))
                  }><DislikeLogo/></div>
          </div>
        </> }
      </div>
      <div>
      {userProfile?.tags?.map((tag, i) => (
          <Chip
            key={i}
            data={tag}
          />
        ))}
      </div>
      <div className={css.galleryContainer}>
        {imagesIsLoading ? <div><Loader/></div> : images?.length > 0 ? 
          <ImageGallery items={images} /> : null}
       </div>
    </>
  );
}

export default ProfileCard
