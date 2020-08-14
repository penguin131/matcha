import React, { useState, useCallback } from "react";
import Gallery from "react-photo-gallery";
import { photos } from "./photos";
import Lightbox from 'react-image-lightbox';
import 'react-image-lightbox/style.css'; // This only needs to be imported once in your app
 
import css from './ProfilePage.module.less'

const ProfilePage = () => {

    const [photoIndex, setPhotoIndex] = useState(0)
    const [isOpen, setIsOpen] = useState(false)
  return (
    <div className={css.kek}>
      <Gallery photos={photos} onClick={() => setIsOpen(true)} />
      <div>
     
 
        {isOpen && (
          <Lightbox
            mainSrc={photos[photoIndex].src}
            nextSrc={photos[(photoIndex + 1) % photos.length].src}
            prevSrc={photos[(photoIndex + photos.length - 1) % photos.length].src}
            onCloseRequest={() => setIsOpen(false)}
            onMovePrevRequest={() =>
              setPhotoIndex((photoIndex + photos.length - 1) % photos.length)
            }
            onMoveNextRequest={() =>
                setPhotoIndex((photoIndex + 1) % photos.length)
            }
          />
        )}
      </div>
    </div>
  );
}

export default ProfilePage
