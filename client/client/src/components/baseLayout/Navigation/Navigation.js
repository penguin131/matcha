import React from 'react'
import { Link } from 'react-router-dom'
import { ReactComponent as ChatsLogo} from '../../../imgs/chats.svg'
import { ReactComponent as MatchLogo} from '../../../imgs/match.svg'
import { ReactComponent as SearchLogo} from '../../../imgs/search.svg'
import { ReactComponent as SettingsLogo} from '../../../imgs/settings.svg'
import css from './Navigation.module.less'

const Navigation = () => {
  return (
    <nav className={css.navContainer}>
      <Link to={'/'}><SearchLogo/></Link>
      <Link to={'/chats'}><ChatsLogo/></Link>
      <Link to={'/'}><MatchLogo/></Link>
      <Link to={'/settings'}><SettingsLogo/></Link>
      <Link to={'/profile/:bfalmer'}>Profile</Link>
    </nav>
  )
}

export default Navigation
