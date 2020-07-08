import axios from 'axios'

const url = 'https://cors-anywhere.herokuapp.com/http://84.38.183.163:8080/spark-server-1.0/'

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
        console.log(res)
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
        console.log(res)
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
