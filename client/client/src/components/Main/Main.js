import React, { useContext } from 'react'
import Header from '../Header/Header'
import Footer from '../Footer/Footer'
import Navigation from '../Navigation/Navigation'
import Aside from '../Aside/Aside'
import MainSection from '../MainSection/MainSection'
import Settings from '../Settings/Settings'
import ChatPage from '../../pages/ChatPage/ChatPage'
import { Switch } from 'react-router-dom'
import ProtectedRoute from '../ProtectedRoute/ProtectedRoute'
import { AuthContext } from '../context/AuthContext'
import css from './Main.module.css'

const Main = () => {
  /* const { isAuth } = useContext(AuthContext) */
  const isAuth = true

  return (
    <div className={css.appContainer}>
      <Header/>
        <main className={css.mainContainer}>
          <Navigation/>
          <Switch>
            <ProtectedRoute path='/chats' component={ChatPage} isAuth={isAuth}/>
            <ProtectedRoute path='/settings' component={Settings} isAuth={isAuth}/>
            <ProtectedRoute exact path='/' component={MainSection} isAuth={isAuth}/>
          </Switch>
          <Aside/>
        </main>
      <Footer/>
    </div>
  )
}

export default Main
