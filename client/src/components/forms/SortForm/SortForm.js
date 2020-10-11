import React from 'react'
import { Form, Field } from 'react-final-form'
import css from './SortForm.module.less'

const SortForm = ({ filters, setFilters }) => {
  return (
    <Form
      onSubmit={e => {
        setFilters({...filters, ...e})
      }}
      initialValues={{
        sort_type: 'rating',
      }}
      render={({handleSubmit, submitting, pristine, values }) => (
        <form onSubmit={handleSubmit}>
          <div className={css.inputsBlock}>
            <Field  name='sort_type'
                    component='input'
                    type='radio'
                    value='age'/> age asc
            <Field  name='sort_type'
                    component='input'
                    type='radio'
                    value='ageDesc'/> age desc
            <Field  name='sort_type'
                    component='input'
                    type='radio'
                    value='distance'/> location
            <Field  name='sort_type'
                    component='input'
                    type='radio'
                    value='rating'/> rating
            <Field  name='sort_type'
                    component='input'
                    type='radio'
                    value='tags'/> tags
            <button type='submit' disabled={submitting || pristine}>
              Save
            </button>
          </div>
          
        </form>
      )}
    />
  )
}

export default SortForm
