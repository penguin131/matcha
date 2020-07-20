import axios from 'axios'
import { url } from './backendUrl'

const token = localStorage.token

export const getAllFriends = async (setIsLoading, setFriendsList) => {
  try {
    setIsLoading(true)
    await axios.get(`${url}protected/getAllFriends`, {
        headers: {
          'Authorization': `${token}`
        } } )
      .then(res => {
        setIsLoading(false) 
        setFriendsList(res.data)
      })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}

export const getUserChatHistory = async (setIsLoading, setMessages, login) => {
    try {
      console.log('start')
      setIsLoading(true)
      await axios.get(`${url}protected/getChatHistory/${login}`, {
          headers: {
            'Authorization': `${token}`
          } } )
        .then(res => {
          setIsLoading(false) 
          setMessages(res.data)
          console.log(res.data)
        })
    } catch(e) {
      setIsLoading(false) 
      console.log(e)
    }
}
