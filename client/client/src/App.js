import React from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/Main/Main'
import AuthPage from './pages/AuthPage/AuthPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import './App.css'

const App = () => {
  const isAuth = true

  return (
    <ErrorBoundary>
      <BrowserRouter>
      <Switch>
        <Route path='/join' component={AuthPage}/>
        <Route path='/login' component={AuthPage}/>
        <ProtectedRoute path='/' component={Main} />
      </Switch>
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App;
