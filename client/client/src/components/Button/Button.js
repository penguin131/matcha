import React from 'react'
import css from './Button.module.less'

const Button = ({type, onClick, label}) => {
  return (
    <button className={css.button} type="submit" onClick={onClick}>
      {label}
    </button>
  )
}

export default Button