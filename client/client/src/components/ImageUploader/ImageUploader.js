import React, {useState} from 'react'
import ImageUploading from "react-images-uploading"
import * as services from '../../services/services'
import cssButton from '../Button/Button.module.less'
import ImageItem from '../ImageItem/ImageItem'

const ImageUploader = ({imgsCount}) => {

  const [images, setImages] = useState([])
  const maxNumber = 5
  const acceptType = ['jpg', 'png']
  const maxFileSize = 3145728

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
        const count = (imageList.length + imgsCount) > maxNumber

        return (
          <div>
            <button className={cssButton.button}
                    onClick={onImageUpload}
                    {...dragProps}
            >Upload image</button>
            {imageList.map((image, i) => (
              <ImageItem
                key={i}
                dataUrl={image.data_url}
                onClick={() => onImageRemove(i)}
              />
            ))}
            <div>
              {errors.maxNumber && <span>
                {`Number of selected images exceed ${maxNumber}`}
              </span>}
              {errors.acceptType && <span>
                {`Your selected file type is not allow ${acceptType.join(', ')}`}
              </span>}
              {errors.maxFileSize && <span>
                {`Selected file size exceed ${maxFileSize / 3145728} mb`}
              </span>}
              {count && <span>
                {`Can store maximum ${maxNumber} images`}
              </span>}
            </div>
            {imageList.length > 0 && imageList.length + imgsCount <= 5 && <button onClick={() => uploadImages(imageList)}>Upload images</button>}
          </div>
        )}}
      </ImageUploading>
    </div>
  )
}

export default ImageUploader
