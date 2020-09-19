import React from 'react'
import css from './ImageItem.module.less'

const ImageItem = ({dataUrl, onClick}) => {
  return (
    <div className={css.imageItem}>
      <img src={dataUrl} alt='uploading img' width='100'/>
      <div>
        <button onClick={onClick}>Remove</button>
      </div>
    </div>
  )
}

export default ImageItem
