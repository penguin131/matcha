import React, { useState } from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/baseLayout/Main/Main'
import AuthPage from './pages/AuthPage/AuthPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import { AuthContext } from './components/context/AuthContext'
import './App.css'

const App = () => {
  const [isAuth, setIsAuth] = useState(true)

  const AuthPageWithContext = () => (
    <AuthContext.Provider value={{setIsAuth}}>
      <AuthPage/>
    </AuthContext.Provider>
  )

  const MainPageWithContext = () =>(
    <AuthContext.Provider value={{isAuth}}>
      <Main/>
    </AuthContext.Provider>
  )

  return (
    <ErrorBoundary>
      <BrowserRouter>
        <Switch>
          <Route path='/join' component={AuthPage}/>
          <Route path='/login' component={AuthPageWithContext}/>
          <ProtectedRoute path='/' component={MainPageWithContext} isAuth={isAuth}/>
        </Switch>
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App;
