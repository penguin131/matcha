import React from 'react'
import css from './Chip.module.less'

const Chip = ({data, onRemove}) => {
  return (
    <div className={css.chip}>
      <div className={css.text}><span>{data}</span></div>
      <div className={css.cross} onClick={onRemove}>x</div>
    </div>
  )
}

export default Chip
