import React from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/Main/Main'
import LoginPage from './components/LoginPage/LoginPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'

function App() {
  return (
    <ErrorBoundary>
      <BrowserRouter>
      <Switch>
      <Route path='/login' component={LoginPage}/>
        <Route path='/' component={Main}/>
      </Switch>
        
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App;
