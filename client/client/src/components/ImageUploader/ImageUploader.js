import React, {useState} from 'react'
import ImageUploading from "react-images-uploading"
import * as services from '../../services/services'
/* import css from './ImageUploader.module.less' */
import ImageItem from '../ImageItem/ImageItem'

const ImageUploader = () => {

  const [images, setImages] = useState([])
  const maxNumber = 5
  const acceptType = ['jpg', 'png']
  const maxFileSize = 5 * 1024 * 1024

  const onChange = (imageList, addUpdateIndex) => {
    setImages(imageList)
  }

  const uploadImages = (imageList) => {
    const urlsList = imageList.map(image => image.data_url)
    services.uploadImages(urlsList)
  }

  return (
    <div>
      <ImageUploading multiple
                      value={images}
                      onChange={onChange}
                      maxNumber={maxNumber}
                      acceptType={acceptType}
                      maxFileSize={maxFileSize}
                      dataURLKey="data_url"
      >{({imageList, onImageUpload, onImageRemove, isDragging, dragProps, errors}) => {
        return (
          <div>
            <button style={isDragging ? {color: 'red'} : null}
                    onClick={onImageUpload}
                    {...dragProps}
            >Click or Drop</button>
            {imageList.map((image, i) => (
              <ImageItem key={i} dataUrl={image.data_url} onClick={() => onImageRemove(i)}/>
            ))}
            <div>
              {errors.maxNumber && <span>
                {`Number of selected images exceed ${maxNumber}`}
              </span>}
              {errors.acceptType && <span>
                {`Your selected file type is not allow ${acceptType.join(', ')}`}
              </span>}
              {errors.maxFileSize && <span>
                {`Selected file size exceed ${maxFileSize / 2048} mb`}
              </span>}
            </div>
            {imageList.length > 0 && <button onClick={() => uploadImages(imageList)}>Upload images</button>}
          </div>
        )}}
      </ImageUploading>
    </div>
  )
}

export default ImageUploader
