import React, {useState} from 'react'
import ImageUploading from "react-images-uploading"
import {uploadImagesUrl} from '../../services/services'
import cssButton from '../Button/Button.module.less'
import ImageItem from '../ImageItem/ImageItem'
import {usePostAxiosFetch} from '../../services/useAxiosFetch'
import css from './ImageUploader.module.less'

const ImageUploader = ({imgsCount}) => {
  const token = localStorage.token
  const config = {headers: {'Authorization': token}}
  const [images, setImages] = useState([])
  const [, uploadImages] = usePostAxiosFetch(config)
  const maxNumber = 5
  const acceptType = ['jpg', 'png']
  const maxFileSize = 3145728

  const onChange = (imageList) => {
    setImages(imageList)
  }

  const uploadImagesRequest = (imageList) => {
    const urlsList = imageList.map(image => image.data_url)
    uploadImages(uploadImagesUrl, urlsList)
  }

  return (
    <div className={css.uploaderBlock}>
      <ImageUploading multiple
                      value={images}
                      onChange={onChange}
                      maxNumber={maxNumber}
                      acceptType={acceptType}
                      maxFileSize={maxFileSize}
                      dataURLKey="data_url"
      >{({imageList, onImageUpload, onImageRemove, dragProps, errors}) => {
        const count = (imageList?.length + imgsCount) > maxNumber

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
                onRemove={() => onImageRemove(i)}
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
            {imageList.length > 0 && imageList.length + imgsCount <= 5 &&
            <button className={cssButton.button}
                    onClick={() => uploadImagesRequest(imageList)}>
              Upload images</button>}
          </div>
        )}}
      </ImageUploading>
    </div>
  )
}

export default ImageUploader
