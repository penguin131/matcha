import React from 'react'
import css from './ImageItem.module.less'
import buttonCss from '../Button/Button.module.less'
import * as services from '../../services/services'

const ImageItem = ({dataUrl, imageId, onClick, withMainSelect}) => {
  return (
    <div className={css.imageItem}>
      <img src={dataUrl} alt='uploading img' width='100'/>
      <div>
        <button onClick={onClick} className={buttonCss.button}>Remove</button>
        {withMainSelect &&
          <button onClick={() => services.setAvatar(imageId)}
                  className={buttonCss.button}>
            Set avatar
          </button>}
      </div>
    </div>
  )
}

export default ImageItem
