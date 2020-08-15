import React, {useState, useEffect } from 'react'
import SettingsForm from '../../components/forms/SettingsForm/SettingsForm'
import css from './SettingsPage.module.less'
import * as services from '../../services/settings.js'
import ImageUploader from "react-images-upload";
import Loader from '../../components/Loader/Loader'

const SettingsPage = ({data}) => {
  const { userProfile, userPhotos } = data
  const [isLoading, setIsLoading] = useState(false)

  const onSubmit = () => {
    console.log('kek')
  }
  
  const onDrop = (photo) => {
    console.log(photo)
  }
  const {
    first_name,
    last_name,
    login,
    geolocation,
  } = userProfile;

  return (
    
    <div className={css.settingsContainer}>
      {isLoading ? <Loader/> : (
      <>
        <div className={css.userName}>
          {`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}
        </div>
        <ImageUploader
          withIcon={true}
          buttonText="Choose images"
          onChange={onDrop}
          imgExtension={[".jpg", ".gif", ".png", ".gif"]}
          maxFileSize={5242880}
        />
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
