import React from 'react'
import SettingsForm from '../../components/forms/SettingsForm/SettingsForm'
import css from './SettingsPage.module.less'
import Loader from '../../components/Loader/Loader'
import ImageUploader from '../../components/ImageUploader/ImageUploader'

const SettingsPage = ({data}) => {
  const { userProfile, userPhotos, isLoading } = data

  const onSubmit = () => {
    console.log('kek')
  }
  
  const {
    first_name,
    last_name,
    login,
    geolocation,
  } = userProfile;

  return (
    
    <div className={css.settingsContainer}>
      {isLoading ? <div className={css.loaderBlock}><Loader/></div> : (
      <>
        <div className={css.userName}>
          {`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}
        </div>
        <ImageUploader/>
        <SettingsForm
          onSubmit={onSubmit}
          data={userProfile}
          isLoading={isLoading}
          geolocation={geolocation}
        />
      </>
    )}
    </div> 
  )
}

export default SettingsPage
