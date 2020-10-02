import React from 'react'
import css from './ImageItem.module.less'
import buttonCss from '../Button/Button.module.less'
import * as services from '../../services/services'

const ImageItem = ({dataUrl, onRemove, onSetAvatar, withMainSelect}) => {

  return (
    <div className={css.imageItem}>
      <img src={dataUrl} alt='uploading img' width='100'/>
      <div>
        <button onClick={onRemove}
                className={buttonCss.button}>
          Remove</button>
        {withMainSelect &&
          <button onClick={onSetAvatar}
                  className={buttonCss.button}>
            Set avatar
          </button>}
      </div>
    </div>
  )
}

export default ImageItem
