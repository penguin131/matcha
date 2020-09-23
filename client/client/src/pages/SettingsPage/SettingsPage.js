import React from 'react'
import SettingsForm from '../../components/forms/SettingsForm/SettingsForm'
import css from './SettingsPage.module.less'
import Loader from '../../components/Loader/Loader'
import ImageUploader from '../../components/ImageUploader/ImageUploader'
import ImageItem from '../../components/ImageItem/ImageItem'
import TagsInput from '../../components/TagsInput/TagsInput'
import * as services from '../../services/services'

const SettingsPage = ({data}) => {
  const { userProfile = {}, userPhotos = [], isLoading = false } = data
  const { first_name, last_name, login, tags } = userProfile;

  return (
    <div className={css.settingsContainer}>
      {isLoading ? <div className={css.loaderBlock}><Loader/></div> : (
      <>
        <div className={css.userName}>
          {`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}
        </div>
        <div className={css.imagesBlock}>
          <div className={css.imagesList}>
            {userPhotos.map((photo, i) => (
              <ImageItem
                key={i}
                dataUrl={photo.data}
                onClick={() => services.deleteImage(photo.imageId)}
                withMainSelect
                imageId={photo.imageId}
              />)
            )}
          </div>
          
          <ImageUploader imgsCount={userPhotos.length}/>
        </div>
        
        <SettingsForm
          onSubmit={services.updateProfile}
          data={userProfile}
        />
        <TagsInput data={tags}/>
      </>
    )}
    </div> 
  )
}

export default SettingsPage
