import React from 'react'
import Navigation from '../Navigation/Navigation'
import Aside from '../Aside/Aside'
import MainSection from '../MainSection/MainSection'
import css from './Main.module.css'

const Main = () => {
    return (
        <main className={css.mainContainer}>
          <Navigation/>
          <MainSection/>
          <Aside/>
        </main>
    )
}

export default Main
