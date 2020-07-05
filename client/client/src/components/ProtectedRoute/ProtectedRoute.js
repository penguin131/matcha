import React from 'react'
import { Route, Redirect } from 'react-router-dom'

const ProtectedRoute = ({ component: Component, isAuth, ...rest}) => {
    console.log(isAuth)
    return (
        <Route
            {...rest}
            render={(props) => {
                if (isAuth) {
                    return ( <Component {...props}/> )
                } else {
                    return <Redirect to={{ pathname: '/join', state: {from: props.location}}}/>
                }
                 
            }
        }/>
    )
}

export default ProtectedRoute
