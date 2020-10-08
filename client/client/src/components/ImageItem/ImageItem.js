import React from 'react'
import css from './ImageItem.module.less'
import buttonCss from '../Button/Button.module.less'

const ImageItem = ({dataUrl, onRemove, onSetAvatar, withMainSelect}) => {

  return (
    <div className={css.imageItem}>
      <img src={dataUrl} alt='uploading img'/>
      <div className={css.contextMenu}>
        <div className={css.contextButtons}>
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
    </div>
  )
}

export default ImageItem
