import React, { useState, useEffect } from 'react'
import Header from '../Header/Header'
import Navigation from '../Navigation/Navigation'
import MainPage from '../../../pages/MainPage/MainPage'
import SettingsPage from '../../../pages/SettingsPage/SettingsPage'
import ChatPage from '../../../pages/ChatPage/ChatPage'
import ProfilePage from '../../../pages/ProfilePage/ProfilePage'
import NotFoundPage from '../../../pages/NotFoundPage/NotFoundPage'
import SearchPage from '../../../pages/SearchPage/SearchPage'
import WatchPage from '../../../pages/WatchPage/WatchPage'
import LikePage from '../../../pages/LikePage/LikePage'
import { Switch, Route } from 'react-router-dom'
import ProtectedRoute from '../../ProtectedRoute/ProtectedRoute'
import css from './Main.module.css'
import { ws } from '../../../services/backendUrl'
import { useGetAxiosFetch, usePostAxiosFetch } from '../../../services/useAxiosFetch'
import { userProfileUrl, userPhotosUrl, updateUserProfileUrl, geolocationServiceUrl } from '../../../services/services'

const token = localStorage.token
const webSocket = new WebSocket(`${ws}${token}`)
const user = localStorage.currentUser

const Main = ({isAuth, setIsAuth}) => {
  const config = {headers: {'Authorization': token}}
  const [userProfile, fetchUserProfile] = useGetAxiosFetch(config)
  const [userPhotos, fetchUserPhotos] = useGetAxiosFetch(config)
  const [{}, getUserGeolocation] = useGetAxiosFetch()
  const [{}, updateUserProfile] = usePostAxiosFetch(config)
  const [notification, setNotification] = useState({})

  useEffect(() => {
    fetchUserProfile(`${userProfileUrl}/${user}`)
    fetchUserPhotos(`${userPhotosUrl}/${user}`)
  }, [fetchUserProfile, fetchUserPhotos])

  useEffect(() => {
    const options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0
    };

    const success = (pos) => {
      const crd = pos.coords;
      const data = {
        latitude: crd.latitude,
        longitude: crd.longitude
      }
      updateUserProfile(updateUserProfileUrl, data)
    };
    
    const error = async (err) => {
      getUserGeolocation(geolocationServiceUrl)
        .then(res => {
          console.log(res)
          const coords = res.data.loc.split(',')
          const data = {
            latitude: coords[0],
            longitude: coords[1]
          }

          updateUserProfile(updateUserProfileUrl, data)
        })
    };

    navigator.geolocation.getCurrentPosition(success, error, options);
  }, [])

  useEffect(() => {
    webSocket.onopen = () => {
      console.log('connected')
    }

    webSocket.onclose = () => {
      console.log('closed')
    }
    
    webSocket.onmessage = (message) => {
      const data = JSON.parse(message.data)
     
        setNotification(data)
   
    }
  
    return () => {
      webSocket.close()
      console.log('closed')
    }
  }, [])

  return (
    <div className={css.appContainer}>
      <Header userProfile={userProfile.data?.data}
              userPhotos={userPhotos.data?.data}
              isLoading={userProfile.loading}
              setIsAuth={setIsAuth}
              notification={notification}/>
        <main className={css.mainContainer}>
          <Navigation/>
          <Switch>
            <ProtectedRoute exact
                            path='/chats'
                            component={() => (
                              <ChatPage webSocket={webSocket}/>
                            )}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/settings'
                            component={() => (
                              <SettingsPage data={{userProfile, userPhotos, isLoading: userProfile.loading}}/>
                            )}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/profile/:login'
                            component={ProfilePage}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/search'
                            component={SearchPage}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/watchedYou'
                            component={WatchPage}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/youLiked'
                            component={LikePage}
                            isAuth={isAuth}/>                            
            <ProtectedRoute exact
                            path='/'
                            component={MainPage}
                            isAuth={isAuth}/>
            <ProtectedRoute exact
                            path='/*'
                            component={NotFoundPage}
                            isAuth={isAuth}/>
          </Switch>
        </main>
    </div>
  )
}

export default Main
