import React from 'react'
import { Link } from 'react-router-dom'
import { ReactComponent as ChatsActiveLogo} from '../../../imgs/chatsActive.svg'
import { ReactComponent as MatchActiveLogo} from '../../../imgs/matchActive.svg'
import { ReactComponent as SearchActiveLogo} from '../../../imgs/searchActive.svg'
import { ReactComponent as SettingsActiveLogo} from '../../../imgs/settingsActive.svg'
import { ReactComponent as ChatsLogo} from '../../../imgs/chats.svg'
import { ReactComponent as MatchLogo} from '../../../imgs/match.svg'
import { ReactComponent as SearchLogo} from '../../../imgs/search.svg'
import { ReactComponent as SettingsLogo} from '../../../imgs/settings.svg'
import css from './Navigation.module.less'

const Navigation = () => {
  const tab = window.location.pathname

  return (
    <nav className={css.navContainer}>
      <Link to={'/'}>
        {tab === '/' ?  <SearchActiveLogo/> : <SearchLogo/>}
      </Link>
      <Link to={'/chats'}>
        {tab === '/chats' ?  <ChatsActiveLogo/> : <ChatsLogo/>}
      </Link>
      <Link to={'/'}>
        {tab === '/' ?  <MatchActiveLogo/> : <MatchLogo/>}
      </Link>
      <Link to={'/settings'}>
        {tab === '/settings' ?  <SettingsActiveLogo/> : <SettingsLogo/>}
      </Link>
    </nav>
  )
}

export default Navigation
