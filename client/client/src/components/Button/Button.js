import React from 'react'
import css from './Button.module.less'

const Button = ({type, onClick, label, disabled, className}) => {
  return (
    <button className={`${css.button} ${className}`}
            type={type}
            onClick={onClick}
            disabled={disabled}>
      {label}
    </button>
  )
}

export default Button