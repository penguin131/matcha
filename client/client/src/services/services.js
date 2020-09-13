import axios from 'axios'
import { url } from './backendUrl'

const token = localStorage.token

export const fetchData = async (setIsLoading, setData, api, id, source) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}/protected/${api}/${id}`, {
      headers: {
        'Authorization': `${token}`
      },
      cancelToken: source.token
    } )
    .then(res => {
      setIsLoading(false) 
      setData(res.data)
    })
  } catch(e) {
    setIsLoading(false) 
    console.log(e)
  }
}

export const join = async (values, setIsLoading) => {
  const data = {
    "login": values.username,
    "password": values.password,
    "email": values.email,  
    "sex": 0
  }
  
  try {
    setIsLoading(true)
    await axios.post(`${url}createUserProfile`,data)
      .then(res => {
        setIsLoading(false)
      })
  } catch(e) {
    console.log(e)
  }
}

export const login = async (values, setIsLoading, history, setIsAuth) => {
  const data = {
    "login": values.username,
    "password": values.password,
  }

  try {
    setIsLoading(true)
    await axios.post(`${url}getToken`,data)
      .then(res => {
        localStorage.currentUser = values.username
        setIsLoading(false)
        setIsAuth(true)
        history.push('/')
        localStorage.setItem('token', res.data)
      })
  } catch(e) {
    setIsLoading(false)
    console.log(e)
  }
}

export const getAllFriends = async (setIsLoading, setFriendsList) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}protected/getAllFriends`, {
        headers: {
          'Authorization': `${token}`
        }
      })
      .then(res => {
        setIsLoading(false) 
        setFriendsList(res.data)
      })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}

export const setLike = async (login) => {
  try {
    await axios.get(`${url}protected/setLike/${login}`, {
      headers: {
        'Authorization': `${token}`
      }
    })
  } catch(e) {
    console.log(e)
  }
} 