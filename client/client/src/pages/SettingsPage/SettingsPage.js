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
  const { userProfile = {}, userPhotos = [], isLoading = false } = data
  const { first_name, last_name, login, tags } = userProfile;
  const [, sendGetRequest] = useGetAxiosFetch(config)
  const [responseData, sendPostRequest] = usePostAxiosFetch(config)

  return (
    <div className={css.settingsContainer}>
      {isLoading ? <div className={css.loaderBlock}><Loader/></div> : (
      <>
        <div className={css.userName}>
          {`${first_name || '-'} ${login || '-'} ${last_name || '-'}`}
        </div>
        <div className={css.imagesBlock}>
          <div className={css.imagesList}>
            {userPhotos?.data?.data?.map((photo, i) => (
              <ImageItem  key={i}
                          dataUrl={photo.data}
                          onRemove={() => sendGetRequest(`${deleteImageUrl}/${photo.imageId}`)}
                          onSetAvatar={() => sendGetRequest(`${setAvatarUrl}/${photo.imageId}`)}
                          withMainSelect/>
              )
            )}
          </div>
          
          <ImageUploader imgsCount={userPhotos?.data?.data?.length}/>
        </div>
        
        <SettingsForm onSubmit={sendPostRequest}
                      url={updateUserProfileUrl}
                      data={userProfile}
                      isFormLoading={responseData.loading}/>
        <TagsInput  onSubmit={sendPostRequest}
                    url={updateUserProfileUrl}
                    data={tags}/>
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
