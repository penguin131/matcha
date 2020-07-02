import { createStore, combineReducers } from 'redux'
import authReducer from './authReducer'

let reducers = combineReducers({
    authReducer,
})

let store = createStore(reducers)

export default store
