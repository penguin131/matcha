import React, { useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { usePostAxiosFetch } from '../services/useAxiosFetch'
import { loginUrl, login } from '../services/services'

const RedirectPage = () => {
  const params = useLocation()
  const key = params.search.split('=')[1]
  const [data, sendPostRequest] = usePostAxiosFetch()
  
  useEffect(() => {
    sendPostRequest(loginUrl, {'oauth2_code': key})
  }, [])

  return (
    <div>Redirect...</div>
  )
}

export default RedirectPage
