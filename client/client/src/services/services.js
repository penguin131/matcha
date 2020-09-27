import axios from 'axios'
import { url, proxy, ipInfoToken } from './backendUrl'

const token = localStorage.token
const user = localStorage.currentUser

export const userProfileForLoginUrl = `${url}protected/getUserProfileForLogin/${user}`
export const userPhotosUrl = `${url}protected/getUserPhotos/${user}`
export const updateUserProfileUrl = `${url}/protected/updateUserProfile`
export const geolocationServiceUrl = `${proxy}https://ipinfo.io?token=${ipInfoToken}`
export const allFriendsUrl = `${url}protected/getAllFriends`
export const chatHistoryUrl = `${url}protected/getChatHistory`
// gets

const axiosGet = async (setData, url, apiToken, source = {}) => {
  let data = {}

  try {
    await axios.get(url, {
      headers: {
        'Authorization': `${apiToken}`
      },
      cancelToken: source.token
    }).then(res => setData(res.data))
  } catch(e) {
    data = e
  }

  return data
}

export const fetchData = async (setData, api, id) => {
  const apiUrl = `${url}protected/${api}/${id}`
  console.log(api)
  const data = await axiosGet(setData, apiUrl, token)

  return (data)
}

export const getAllFriends = async (setData, source) => {
  const apiUrl = `${url}protected/getAllFriends`
  const data = await axiosGet(setData, apiUrl, token, source)

  return (data)
}

export const setLikeDislike = async (login, payload) => {
  const apiUrl = `${url}protected/${payload}/${login}`
  const data = await axiosGet(apiUrl, token)

  return (data)
}

export const getGeolocation = async () => {
  try {
    await axios.get(`${proxy}https://ipinfo.io?token=${ipInfoToken}`)
      .then(res => {
        const data = res.data.loc.split(',')
        updateProfile({
          latitude: data[0],
          longitude: data[1],
        })
      });
  } catch(e) {
    console.log(e)
  }
}

export const deleteImage = async (id) => {
  const apiUrl = `${url}protected/deleteImage/${id}`
  const data = await axiosGet(apiUrl, token)

  return (data)
}

export const setAvatar = async (id) => {
  const apiUrl = `${url}protected/setAvatar/${id}`
  const data = await axiosGet(apiUrl, token)

  return (data)
}

// posts

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

export const updateProfile = async (values, setIsLoading = () => {}) => {
  try {
    await axios.post(`${url}/protected/updateUserProfile`, values, {
      headers: {
        'Authorization': `${token}`
      }
    })
  } catch(e) {
    console.log(e)
  }
}

export const uploadImages = async (values, setIsLoading = () => {}) => {
  try {
    await axios.post(`${url}/protected/downloadImage`, values, {
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/upload',
      }
    })
  } catch(e) {
    console.log(e)
  }
}

export const getNextUser = async (values, setIsLoading = () => {}, setData) => {
  setIsLoading(true)
  try {
    await axios.post(`${url}/protected/getNextUser`, values, {
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/upload',
      }
    }).then(res => setData(res.data))
  } catch(e) {
    console.log(e)
  }
  setIsLoading(false)
}
