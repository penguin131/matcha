import React, {useEffect, useState} from 'react'
import Loader from '../../components/Loader/Loader'
import css from './ProfileCard.module.less'
import {ReactComponent as LikeLogo} from '../../imgs/like.svg'
import {ReactComponent as DislikeLogo} from '../../imgs/dislike.svg'
import ImageGallery from 'react-image-gallery';
import Chip from '../TagsInput/Chip/Chip'
import {setLikeUrl, setDislikeUrl, reportUrl} from '../../services/services'
import {useGetAxiosFetch} from '../../services/useAxiosFetch'
import { Link } from 'react-router-dom'
import moment from 'moment'

const ProfileCard = ({user, userProfile, userPhotos, profileIsLoading, imagesIsLoading, disableLike, match}) => {
  const token = localStorage.token
  const config = {headers: {'Authorization': token}}
  const [, sendGetRequest] = useGetAxiosFetch(config)
  const [likeStatus, setLikeStatus] = useState(null)
  const [viewBio, setViewBio] = useState(false)
  const images = userPhotos?.data?.map(photo => ({
    original: photo.data,
    thumbnail: photo.data,
    originalClass: css.galleryImage,
    thumbnailClass: css.thumbnailImage,
    main: photo.data.main
  })) 
  
  const formatDate = (date) => moment(date).format('MMM Do')

  const onLike = async (user) => {
    await sendGetRequest(`${setLikeUrl}/${user}`)
    setLikeStatus('liked')
  }

  const onDislike = async () => {
    await sendGetRequest(`${setDislikeUrl}/${user}`)
    setLikeStatus('disliked')
  }

  useEffect(() => {
    setLikeStatus(userProfile?.has_like ? 'liked' : userProfile?.has_dislike ? 'disliked' : null)
  }, [userProfile])

  return (
    <>
      <div className={css.profileInfoContainer}>
        {profileIsLoading ? <div className={css.loader}><Loader/></div> : <>
          
          {match?.path !== '/profile/:login' ? <Link to={`profile/:${userProfile?.login}`}>
            <div className={css.userName}>
              {`${userProfile?.first_name || '-'} ${userProfile?.login || '-'} ${userProfile?.last_name || '-'}`}
            </div>
          </Link> : <div className={css.userName}></div>}
          <div>{userProfile?.is_online ? 'Online' : `Last login ${formatDate(userProfile?.last_auth_date)}`}</div>
          <div>rating: {userProfile?.rating}</div>
          <div>sex: {userProfile?.sex}</div>
          <div className={css.galleryContainer}>
            {imagesIsLoading ? <div className={css.loader}><Loader/></div> : images?.length > 0 ? 
              <ImageGallery items={images}
                            showNav={images.length > 0}
                            showFullscreenButton={false}
                            showPlayButton={false}
                            showThumbnails={false}/> : null}
          </div>
          {!disableLike && <div className={css.likePanel}>
            <div  className={`${css.likeElement} ${likeStatus === 'liked' ? css.activeLike : css.like}`}
                  onClick={() => onLike(user)}>
                    <LikeLogo/></div>
            <div  className={`${css.likeElement} ${likeStatus === 'disliked'? css.activeDislike : css.dislike}`}
                  onClick={() => onDislike(user)}>
                    <DislikeLogo/></div>
          </div>}
          <div  className={css.bioBlock}
                onClick={() => setViewBio(!viewBio)}>
            {`---${viewBio ? 'hide' : 'show more'}---`}
          </div>
          {viewBio && <>
            <div className={css.tags}>
              {userProfile?.tags?.map((tag, i) => (
                <Chip key={i}
                      data={tag}/>
              ))}
            </div>
            <div className={css.userInfo}>
              {`${userProfile?.biography || '-'}`}
            </div>
            <button type="button" onClick={() => sendGetRequest(`${reportUrl}/${userProfile.login}`)}>Report</button>
          </>}
        </>}
      </div>
    </>
  );
}

export default ProfileCard
