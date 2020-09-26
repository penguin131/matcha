import React, {useState, useEffect} from 'react'
import Header from '../Header/Header'
import Navigation from '../Navigation/Navigation'
import MainPage from '../../../pages/MainPage/MainPage'
import SettingsPage from '../../../pages/SettingsPage/SettingsPage'
import ChatPage from '../../../pages/ChatPage/ChatPage'
import ProfilePage from '../../../pages/ProfilePage/ProfilePage'
import NotFoundPage from '../../../pages/NotFoundPage/NotFoundPage'
import SearchPage from '../../../pages/SearchPage/SearchPage'
import { Switch } from 'react-router-dom'
import ProtectedRoute from '../../ProtectedRoute/ProtectedRoute'
import * as services from '../../../services/services'
import axios from 'axios'
import css from './Main.module.css'
import { ws } from '../../../services/backendUrl'

const token = localStorage.token
const webSocket = new WebSocket(`${ws}${token}`)

const Main = ({isAuth, setIsAuth}) => {
  const [isLoading, setIsLoading] = useState(false)
  const [userProfile, setUserProfile] = useState({})
  const [userPhotos, setUserPhotos] = useState([])
  const user = localStorage.currentUser
 

  useEffect(() => {
    webSocket.onmessage = (message) => {
      const data = JSON.parse(message.data)
      if (data.type === 'notification') {
        console.log(data)
      }
    }
  }, [])

  useEffect(() => {  
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source()

    Promise.all([
      services.fetchData(setIsLoading, setUserProfile, 'getUserProfileForLogin', user, source),
      services.fetchData(setIsLoading, setUserPhotos, 'getUserPhotos', user, source),
    ])
  
    return () => {
      source.cancel();
    };
  }, [user])
  
  useEffect(() => {
    const options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0
    };

    const success = (pos) => {
      const crd = pos.coords;
      services.updateProfile({
        latitude: crd.latitude,
        longitude: crd.longitude
      })
    };
    
    const error = async (err) => {
      services.getGeolocation()
    };

    navigator.geolocation.getCurrentPosition(success, error, options);
  }, [])

  return (
    <div className={css.appContainer}>
      <Header data={{userProfile, userPhotos}}
              isLoading={isLoading}
              setIsAuth={setIsAuth}/>
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
                              <SettingsPage data={{userProfile, userPhotos, isLoading}}/>
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
