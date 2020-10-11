import {useState, useMemo, useCallback, useEffect} from 'react'
import axios from 'axios'
import _ from 'lodash'

export const useGetAxiosFetch = (config = {}) => {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const source = useMemo(() => axios.CancelToken.source(), [])

  const sendRequest = useCallback(async (url, _config = {}) => {
    if (!url) {
      return
    }
    try {
      setLoading(true)

      const sendRequest = (url) => axios
          .get(url, {cancelToken: source.token, ...config, ..._config})
          .catch((err) => {
            if (axios.isCancel(err)) {
              console.log(`request cancelled: ${err.message}`)
            }
            else {
              console.log('another error happened')
            }
          })

      const promise = _.isArray(url) ? Promise.all(url.map(u => sendRequest(u))) : sendRequest(url)
      const a = await promise

      if (!a) {
        return
      }

      setData(a)
      setLoading(false)

      return a
    }
    catch (e) {
      setData(null)
      setError(e)
    }
  }, [setData, setLoading])

  useEffect(() => {
    return () => source.cancel('Unmounted')
  }, [])

  return [{data, loading, error}, sendRequest]
}

export const usePostAxiosFetch = (config = {}) => {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const source = useMemo(() => axios.CancelToken.source(), [])

  const sendRequest = useCallback(async (url, data) => {
    if (!url || !data) {
      return
    }
    try {
      setLoading(true)

      const a = await axios
          .post(url, data, {cancelToken: source.token, ...config})
          .catch((err) => {
            if (axios.isCancel(err)) {
              console.log(`request cancelled: ${err.message}`)
            }
            else {
              console.log('another error happened')
            }
          })

      if (!a) {
        return
      }

      setData(a.data)
      setLoading(false)

      return a
    }
    catch (e) {
      setData(null)
      setError(e)
    }
  }, [setData, setLoading])


  useEffect(() => {
    return () => source.cancel('Unmounted')
  }, [])

  return [{data, loading, error}, sendRequest]
}