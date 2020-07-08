import React from 'react'
import { Link } from 'react-router-dom'
import css from './Navigation.module.css'

const Navigation = () => {
    return (
        <nav className={css.navContainer}>
            <Link to={'/chats'}>Chats</Link>
            <Link to={'/settings'}>Settings</Link>
        </nav>
    )
}

export default Navigation
