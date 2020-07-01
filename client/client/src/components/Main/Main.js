import React from 'react'
import Header from '../Header/Header'
import Footer from '../Footer/Footer'
import Navigation from '../Navigation/Navigation'
import Aside from '../Aside/Aside'
import MainSection from '../MainSection/MainSection'
import Settings from '../Settings/Settings'
import { Route, Switch } from 'react-router-dom'
import css from './Main.module.css'

const Main = () => {
  return (
    <div className={css.appContainer}>
      <Header/>
        <main className={css.mainContainer}>
          <Navigation/>
          <Switch>
            <Route path='/settings' component={Settings}/>
            <Route path='/' component={MainSection}/>
          </Switch>
          <Aside/>
        </main>
      <Footer/>
    </div>
  )
}

export default Main
