import axios from 'axios'
import { url, proxy, ipInfoToken } from './backendUrl'

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

export const setLikeDislike = async (login, payload) => {
  try {
    await axios.get(`${url}protected/${payload}/${login}`, {
      headers: {
        'Authorization': `${token}`
      }
    })
  } catch(e) {
    console.log(e)
  }
}

export const getGeolocation = async () => {
  try {
    await axios.get(`${proxy}https://ipinfo.io?token=${ipInfoToken}`, (res) => {}, "jsonp")
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

export const deleteImage = async (id) => {
  try {
    await axios.get(`${url}protected/deleteImage/${id}`, {
      headers: {
        'Authorization': `${token}`
      }
    })
  } catch(e) {
    console.log(e)
  }
}

export const setAvatar = async (id) => {
  try {
    await axios.get(`${url}protected/setAvatar/${id}`, {
      headers: {
        'Authorization': `${token}`
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
