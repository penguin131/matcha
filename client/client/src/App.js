import React, { useState, useRef, useEffect } from 'react';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary'
import Main from './components/baseLayout/Main/Main'
import AuthPage from './pages/AuthPage/AuthPage'
import { Route, BrowserRouter, Switch} from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import { AuthContext } from './components/context/AuthContext'
import './App.css'


const App = () => {
  const [isAuth, setIsAuth] = useState(true)
  const token = localStorage.token
  const url = '84.38.183.163:8080/chat'
  const webSocket = useRef(null)
  const [messages, setMessages] = useState([])
  useEffect(() => {
      webSocket.current = new WebSocket(`ws://${url}?token=${token}`)
      webSocket.current.onopen = () => {
          // on connecting, do nothing but log it to the console
          console.log('connected')
      }
      webSocket.current.onmessage = (message) => {
          console.log(message)
      }
      return () => {
          webSocket.current.close()
          console.log('closed')
      }
  }, [])
  const AuthPageWithContext = () => (
    <AuthContext.Provider value={{setIsAuth}}>
      <AuthPage/>
    </AuthContext.Provider>
  )

  const MainPageWithContext = () => (
    <AuthContext.Provider value={{isAuth, messages, setMessages, webSocket}}>
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
