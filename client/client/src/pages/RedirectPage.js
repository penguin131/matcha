import React, { useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { usePostAxiosFetch } from '../services/useAxiosFetch'
import { loginUrl } from '../services/services'
import { useHistory } from 'react-router-dom'

const RedirectPage = ({ setIsAuth }) => {
  const params = useLocation()
  const key = params.search.split('=')[1]
  const [, sendPostRequest] = usePostAxiosFetch()
  const history = useHistory()

  useEffect(() => {
    key && sendPostRequest(loginUrl, {'oauth2_code': key})
      .then(r => {
        if (r?.data) {
          localStorage.setItem('token', r.data.token)
          localStorage.setItem('currentUser', r.data.login)
          history.push('/')
          setIsAuth(true)
        } 
      })
  }, [key, history, sendPostRequest, setIsAuth])

  return (
    <div>Redirect...</div>
  )
}

export default RedirectPage
