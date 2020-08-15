import React from 'react'
import css from './Footer.module.less'

const Footer = () => {
  return (
    <footer className={css.footerContainer}>
      <div className={css.lol}></div>
      <div className={css.developersInfo}>{`made by: smight & bfalmer`}</div>
    </footer>
  )
}

export default Footer
