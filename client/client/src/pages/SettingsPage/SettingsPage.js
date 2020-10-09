import React from 'react'
import SettingsForm from '../../components/forms/SettingsForm/SettingsForm'
import ChangeEmailForm from '../../components/forms/ChangeEmailForm/ChangeEmailForm'
import ChangePassForm from '../../components/forms/ChangePassForm/ChangePassForm'
import css from './SettingsPage.module.less'
import Loader from '../../components/Loader/Loader'
import ImageUploader from '../../components/ImageUploader/ImageUploader'
import ImageItem from '../../components/ImageItem/ImageItem'
import TagsInput from '../../components/TagsInput/TagsInput'
import {useGetAxiosFetch, usePostAxiosFetch} from '../../services/useAxiosFetch'
import {deleteImageUrl, setAvatarUrl, updateUserProfileUrl} from '../../services/services'

const SettingsPage = ({data}) => {
  const token = localStorage.token
  const config = {headers: {'Authorization': token}}
  const { userProfile, userPhotos} = data
  const [, sendGetRequest] = useGetAxiosFetch(config)
  const [responseData, sendPostRequest] = usePostAxiosFetch(config)
  
  
  return (
    <div className={css.settingsContainer}>
      {userProfile.loading ? <div className={css.loaderBlock}><Loader/></div> : (
      <>
        <div className={css.userName}>
          {`${userProfile?.data?.data.first_name || '-'} ${userProfile?.data?.data.login || '-'} ${userProfile?.data?.data.last_name || '-'}`}
        </div>
        <div className={css.imagesBlock}>
          <div className={css.imagesList}>
            {userPhotos?.data?.data?.map((photo, i) => (
              <ImageItem  key={i}
                          dataUrl={photo.data}
                          onRemove={() => sendGetRequest(`${deleteImageUrl}/${photo.imageId}`)}
                          onSetAvatar={() => sendGetRequest(`${setAvatarUrl}/${photo.imageId}`)}
                          withMainSelect/>)
            )}
          </div>
          
          <ImageUploader imgsCount={userPhotos?.data?.data?.length}/>
        </div>
        
        {userProfile.data && <SettingsForm onSubmit={sendPostRequest}
                      url={updateUserProfileUrl}
                      data={userProfile.data.data}
                      isFormLoading={responseData.loading}/>}
        {userProfile.data && <TagsInput  onSubmit={sendPostRequest}
                    url={updateUserProfileUrl}
                    data={userProfile.data.data.tags}/>}
        <ChangeEmailForm  onSubmit={sendPostRequest}
                          isFormLoading={responseData.loading}/>
        <ChangePassForm onSubmit={sendPostRequest}
                        url={updateUserProfileUrl}
                        isFormLoading={responseData.loading}/>
      </>
    )}
    </div> 
  )
}

export default SettingsPage
