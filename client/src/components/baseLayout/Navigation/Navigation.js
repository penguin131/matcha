import React from 'react'
import { Link } from 'react-router-dom'
import { ReactComponent as ChatsActiveLogo} from '../../../imgs/chatsActive.svg'
import { ReactComponent as WatchActiveLogo} from '../../../imgs/watchActive.svg'
import { ReactComponent as SearchActiveLogo} from '../../../imgs/searchActive.svg'
import { ReactComponent as SettingsActiveLogo} from '../../../imgs/settingsActive.svg'
import { ReactComponent as LikedActiveLogo} from '../../../imgs/matchActive.svg'
import { ReactComponent as ChatsLogo} from '../../../imgs/chats.svg'
import { ReactComponent as WatchLogo} from '../../../imgs/watch.svg'
import { ReactComponent as SearchLogo} from '../../../imgs/search.svg'
import { ReactComponent as SettingsLogo} from '../../../imgs/settings.svg'
import { ReactComponent as LikedLogo} from '../../../imgs/match.svg'
import { ReactComponent as GeoLogo} from '../../../imgs/geo.svg'
import { ReactComponent as GeoActiveLogo} from '../../../imgs/geoActive.svg'
import css from './Navigation.module.less'

const Navigation = () => {
  const tab = window.location.pathname

  return (
    <nav className={css.navContainer}>
      <Link to={'/search'}>
        {tab === '/search' ?  <SearchActiveLogo/> : <SearchLogo/>}
      </Link>
      <Link to={'/chats'}>
        {tab === '/chats' ?  <ChatsActiveLogo/> : <ChatsLogo/>}
      </Link>
      <Link to={'/watchedYou'}>
        {tab === '/watchedYou' ?  <WatchActiveLogo/> : <WatchLogo/>}
      </Link>
      <Link to={'/youLiked'}>
        {tab === '/youLiked' ?  <LikedActiveLogo/> : <LikedLogo/>}
      </Link>
      <Link to={'/map'}>
        {tab === '/map' ?  <GeoActiveLogo/> : <GeoLogo/>}
      </Link>
      <Link to={'/settings'}>
        {tab === '/settings' ?  <SettingsActiveLogo/> : <SettingsLogo/>}
      </Link>
    </nav>
  )
}

export default Navigation
