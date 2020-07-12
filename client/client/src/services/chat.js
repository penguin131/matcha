import axios from 'axios'
import url from './backendUrl'

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
        
        console.log(e)
      }
}

export const getUser = async (setIsLoading, setMessages, login) => {
    try {
        setIsLoading(true)
        await axios.get(`${url}protected/getUserProfileForLogin/${login}`, {
            headers: {
              'Authorization': `${token}`
            } } )
          .then(res => {
            setIsLoading(false) 
            console.log(res.data)
            setMessages(res.data)
          })
      } catch(e) {
        
        console.log(e)
      }
}
