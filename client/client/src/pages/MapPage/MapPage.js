import React, {useEffect} from 'react';
import GoogleMapReact from 'google-map-react';
import css from './MapPage.module.less'
import { useGetAxiosFetch } from '../../services/useAxiosFetch'
import { coordinatesUrl } from '../../services/services'
import { ReactComponent as Marker } from '../../imgs/geo.svg'

const  UserMarker = () => <div><Marker/></div>;

const MapPage = ({geolocation}) => {
  const token = localStorage.token
  const config = {headers: {'Authorization': token}}

  const [allUsers, fetchAllusers] = useGetAxiosFetch(config)
  const center = {
    lat: geolocation?.latitude || 30,
    lng: geolocation?.longitude || 30
  }
  const zoom = 1

  useEffect(() => {
    fetchAllusers(coordinatesUrl)
  }, [fetchAllusers])

  return (
    // Important! Always set the container height explicitly
    <div className={css.mapContainer}>
    <div style={{ height: '60vh', width: '60%', marginTop: '20px'}}>
      <GoogleMapReact
        bootstrapURLKeys={{ key: 'AIzaSyD2OD8350hDoltB6ufRs4lfW7mx8GccIqU' }}
        defaultCenter={center}
        defaultZoom={zoom}>
        {allUsers?.data?.data?.map((user, i) => (
          <UserMarker key={i}
                      lat={user.latitude}
                      lng={user.longitude}
          />
        ))}
        
      </GoogleMapReact>
    </div>
    </div>
  );
}

export default MapPage;
