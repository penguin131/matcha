import React from 'react';
import css from './App.module.css';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Header from './components/Header/Header'
import Main from './components/Main/Main'
import Footer from './components/Footer/Footer'

function App() {
  return (
    <ErrorBoundary>
      <div className={css.appContainer}>
        <Header/>
        <Main/>
        <Footer/>
      </div>
    </ErrorBoundary>
  )
}

export default App;
