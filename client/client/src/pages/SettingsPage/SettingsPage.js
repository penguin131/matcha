import React, {useState, useEffect } from 'react'
import SettingsForm from '../../components/forms/SettingsForm/SettingsForm'
import css from './SettingsPage.module.less'
import * as services from '../../services/settings.js'

const SettingsPage = () => {
  const [isLoading, setIsLoading] = useState(false)
  const [userProfile, setUserProfile] = useState({})
  const [geolocation, setGeolocation] = useState({  latitude: 0, longitude: 0 })
  const user = localStorage.currentUser
  const onSubmit = () => {
    console.log('kek')
  }

  var options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0
  };
  
 
  

  useEffect(() => {
    services.getUserProfile(setIsLoading, setUserProfile, user )
  }, [])
  useEffect(() => {
    const success = (pos) => {
      const crd = pos.coords;
      setGeolocation({
        latitude: crd.latitude,
        longitude: crd.longitude
      })
    };
    
    const error = (err) => {
      console.warn(`ERROR(${err.code}): ${err.message}`);
    };

    navigator.geolocation.getCurrentPosition(success, error, options);
  }, [])

  return (
    <div className={css.settingsContainer}>
      <SettingsForm
        onSubmit={onSubmit}
        data={userProfile}
        isLoading={isLoading}
        geolocation={geolocation}
      />
    </div> 
  )
}

export default SettingsPage
