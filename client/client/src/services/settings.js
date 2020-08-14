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