import React, { useState } from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/baseLayout/Main/Main'
import AuthPage from './pages/AuthPage/AuthPage'
import RedirectPage from './pages/RedirectPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import './App.css'

const App = () => {
const currentUser = localStorage.getItem('currentUser')
const [isAuth, setIsAuth] = useState(currentUser ? true : false)

  return (
    <ErrorBoundary>
      <BrowserRouter>
        <Switch>
          <Route  path='/join'
                  component={() => <AuthPage setIsAuth={setIsAuth}/>}/>
          <Route  path='/login'
                  component={() => <AuthPage setIsAuth={setIsAuth}/>}/>
          <Route  path='/redirect'
                  component={() => <RedirectPage setIsAuth={setIsAuth}/>}/>
          <ProtectedRoute path='/'
                          component={() => <Main isAuth={isAuth} setIsAuth={setIsAuth}/>} isAuth={isAuth}/>
        </Switch>
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App;
