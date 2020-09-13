import React, {useState, useEffect} from 'react'
import Header from '../Header/Header'
import Footer from '../Footer/Footer'
import Navigation from '../Navigation/Navigation'
import Aside from '../Aside/Aside'
import MainPage from '../../../pages/MainPage/MainPage'
import SettingsPage from '../../../pages/SettingsPage/SettingsPage'
import ChatPage from '../../../pages/ChatPage/ChatPage'
import ProfilePage from '../../../pages/ProfilePage/ProfilePage'
import NotFoundPage from '../../../pages/NotFoundPage/NotFoundPage'
import { Switch } from 'react-router-dom'
import ProtectedRoute from '../../ProtectedRoute/ProtectedRoute'
import Loader from '../../Loader/Loader'
import * as services from '../../../services/services'
import axios from 'axios'
/* import { AuthContext } from '../../context/AuthContext' */
import css from './Main.module.css'

const Main = () => {
  /* const { isAuth } = useContext(AuthContext) */
  const isAuth = true
  const [isLoading, setIsLoading] = useState(false)
  const [userProfile, setUserProfile] = useState({})
 // const [geolocation, setGeolocation] = useState({  latitude: 0, longitude: 0 })
  const [userPhotos, setUserPhotos] = useState([])
  const user = localStorage.currentUser

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
  }, [])
  
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

  useEffect(() => {
    services.updateProfile()
  }, )
  return (
    <div className={css.appContainer}>
      <Header data={{userProfile, userPhotos}}/>
        <main className={css.mainContainer}>
          <Navigation/>
          {isLoading ? <Loader/> : (
            <Switch>
              <ProtectedRoute exact path='/chats' component={ChatPage} isAuth={isAuth}/>
              <ProtectedRoute exact
                              path='/settings'
                              component={() => <SettingsPage data={{userProfile, userPhotos}}/>}
                              isAuth={isAuth}
              />
              <ProtectedRoute exact path='/profile/:login' component={ProfilePage} isAuth={isAuth}/>
              <ProtectedRoute exact path='/' component={MainPage} isAuth={isAuth}/>
              <ProtectedRoute exact path='/*' component={NotFoundPage} isAuth={isAuth}/>
            </Switch>
          )} 
          <Aside/>
        </main>
      <Footer/>
    </div>
  )
}

export default Main
