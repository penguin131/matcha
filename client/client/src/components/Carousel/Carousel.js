import React from 'react'
import { Carousel } from "react-responsive-carousel";
import { ip } from '../../services/backendUrl'
import css from './Carousel.module.less'
import "react-responsive-carousel/lib/styles/carousel.min.css";

export default ({ photos }) => (
  <div className={css.customImg}>
    <Carousel autoPlay>
      {photos.map((photo, i) => {
        return (
          <div key={i}>
            <img alt=""  src={`http://${ip}/image/${photo.imageId}`} />
          </div>
        )
      })}
    </Carousel>
  </div>
);
