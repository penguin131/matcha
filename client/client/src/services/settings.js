import axios from 'axios'
import { url } from './backendUrl'

const token = localStorage.token

export const getUserProfile = async (setIsLoading, setUserProfile, login) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}/protected/getUserProfileForLogin/${login}`, {
        headers: {
          'Authorization': `${token}`
        } } )
      .then(res => {
        setIsLoading(false) 
        setUserProfile(res.data)
      })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}

export const getUserPhotos = async (setIsLoading, setUserPhotos, login) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}/protected/getUserPhotos/${login}`, {
        headers: {
          'Authorization': `${token}`
        } } )
      .then(res => {
        setIsLoading(false) 
        setUserPhotos(res.data)
      })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}

export const getUserPhotoById = async (setIsLoading, setUserPhoto, id) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}/image/${id}`, {
        headers: {
          'Authorization': `${token}`
        } } )
      .then(res => {
        setIsLoading(false) 
        setUserPhoto(res.data)
      })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}