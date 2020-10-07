import axios from 'axios'
import { url, proxy, ipInfoToken } from './backendUrl'

export const updateUserProfileUrl = `${url}protected/updateUserProfile`
export const geolocationServiceUrl = `${proxy}https://ipinfo.io?token=${ipInfoToken}`
export const allFriendsUrl = `${url}protected/getAllFriends`
export const chatHistoryUrl = `${url}protected/getChatHistory`
export const userProfileUrl = `${url}protected/getUserProfileForLogin`
export const userPhotosUrl = `${url}protected/getUserPhotos`
export const nextUserProfileUrl = `${url}protected/getNextUser`
export const uploadImagesUrl = `${url}protected/downloadImage`
export const deleteImageUrl = `${url}protected/deleteImage`
export const setAvatarUrl = `${url}protected/setAvatar`
export const setLikeUrl = `${url}protected/setLike`
export const setDislikeUrl = `${url}protected/setComplaint`
export const likedUsersUrl = `${url}protected/getAllLikedUsers`
export const lookedUsersUrl = `${url}protected/getAllLookedUsers`
export const loginUrl = `${url}getToken`
/* 
export const joinUrl = `${url}createUserProfile` */

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
        localStorage.setItem('token', res.data.token)
        
        setIsLoading(false)
        setIsAuth(true)
        
      }).then(() => history.push('/'))
  } catch(e) {
    setIsLoading(false)
    console.log(e)
  }
}
