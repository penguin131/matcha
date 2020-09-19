import React, {useState} from 'react'
import ImageUploading from "react-images-uploading"
/* import css from './ImageUploader.module.less' */

const ImageUploader = () => {

  const [images, setImages] = useState([])
  const maxNumber = 5
  const acceptType = ['jpg', 'png']
  const maxFileSize = 5 * 2048

  const onChange = (imageList, addUpdateIndex) => {
    console.log(imageList[0].data_url)
    setImages(imageList)
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
      >{({imageList, onImageUpload, onImageRemoveAll, onImageRemove, isDragging, dragProps, errors}) => {
        console.log(imageList)
        return (
          <div>
            <button style={isDragging ? {color: 'red'} : null}
                    onClick={onImageUpload}
                    {...dragProps}
            >Click or Drop</button>
            <button onClick={onImageRemoveAll}>Remove all</button>
            {imageList.map((image, i) => (
              <div key={i}>
                <img src={image.data_url} alt='uploading img' width='100'/>
                <div>
                  <button onClick={() => onImageRemove(i)}>Remove</button>
                </div>
              </div>
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
          </div>
        )}}
      </ImageUploading>
    </div>
  )
}

export default ImageUploader
