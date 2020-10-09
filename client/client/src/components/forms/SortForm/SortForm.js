import React from 'react'
import { Form, Field } from 'react-final-form'

const SortForm = ({ filters, setFilters }) => {
  return (
    <Form
      onSubmit={e => {
        setFilters({...filters, ...e})
      }}
      initialValues={{
        sort: 'age',
      }}
      render={({handleSubmit, submitting, pristine, values }) => (
        <form onSubmit={handleSubmit}>
          <div>
            <Field  name='sort'
                    component='input'
                    type='radio'
                    value='age'/> age
            <Field  name='sort'
                    component='input'
                    type='radio'
                    value='location'/> location
            <Field  name='sort'
                    component='input'
                    type='radio'
                    value='fameRating'/> fame rating
            <Field  name='sort'
                    component='input'
                    type='radio'
                    value='commotTags'/> commot tags
          </div>
          <button type='submit' disabled={submitting || pristine}>
              Save
          </button>
        </form>
      )}
    />
  )
}

export default SortForm
