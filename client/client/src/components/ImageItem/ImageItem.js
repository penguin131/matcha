import React from 'react'
import css from './ImageItem.module.less'
import * as services from '../../services/services'

const ImageItem = ({dataUrl, imageId, onClick, withMainSelect}) => {
  return (
    <div className={css.imageItem}>
      <img src={dataUrl} alt='uploading img' width='100'/>
      <div>
        <button onClick={onClick}>Remove</button>
        {withMainSelect && <button onClick={() => services.setAvatar(imageId)}>Set avatar</button>}
      </div>
    </div>
  )
}

export default ImageItem
