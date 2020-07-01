import React from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/Main/Main'
import JoinPage from './components/JoinPage/JoinPage'
import LoginPage from './components/LoginPage/LoginPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import './App.css'

const App = () => {
  const isAuth = true

  return (
    <ErrorBoundary>
      <BrowserRouter>
      <Switch>
        <Route path='/join' component={JoinPage}/>
        <Route path='/login' component={LoginPage}/>
        <ProtectedRoute path='/' component={Main} />
      </Switch>
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App;
