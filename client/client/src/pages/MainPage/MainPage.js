import React from 'react'
import css from './MainPage.module.less'
import { ReactComponent as ChatsLogo} from '../../imgs/chats.svg'
import { ReactComponent as WatchLogo} from '../../imgs/watch.svg'
import { ReactComponent as SearchLogo} from '../../imgs/search.svg'
import { ReactComponent as SettingsLogo} from '../../imgs/settings.svg'
import { ReactComponent as LikedLogo} from '../../imgs/match.svg'

const MainPage = () => {
  return (
    <section className={css.mainSectionContainer}>
      <div className={css.title}>Here you will find: </div>
      <div className={css.element}><div><SearchLogo/></div> - New people</div>
      <div className={css.element}><div><ChatsLogo/></div> - Chats with the matched people</div>
      <div className={css.element}><div><WatchLogo/></div> - People who have reviewed your profile</div>
      <div className={css.element}><div><LikedLogo/></div> - Your profile settings </div>
      <div className={css.element}><div><SettingsLogo/></div> - People you like</div>
    </section>
  )
}

export default MainPage
