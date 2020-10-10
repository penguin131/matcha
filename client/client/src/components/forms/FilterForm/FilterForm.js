import React from 'react'
import { Form, Field } from 'react-final-form'
import css from './FilterForm.module.less'

const NumberInput = ({ input, meta, ...rest }) => (
  <input {...input} {...rest} type="number" min='0'/>
);

const FilterForm = ({ filters, setFilters }) => {
  return (
    <Form
      onSubmit={e => {
        const tagsArr = e.tags.replace(/\s+/g, '').trim().split(',')
        setFilters({...filters, ...e, tags: tagsArr})
      }}
      initialValues={{}}
      render={({handleSubmit, submitting, pristine, values }) => (
        <form onSubmit={handleSubmit}>
          <div className={css.inputsBlock}>
            <Field  name="ageGap"
                    component={NumberInput}
                    placeholder="Age gap"/>
            <Field  name="ratingGap"
                    component={NumberInput}
                    placeholder="Rating gap"/>
            <Field  name='tags'
                    component='input'
                    value='fameRating'
                    placeholder="Tags by commas"/>
            <Field  name="locationGap"
                    component={NumberInput}
                    placeholder="Location gap"/>
          <button type='submit' disabled={submitting || pristine}>
            Save
          </button>
          </div>  
        </form>
      )}
    />
  )
}

export default FilterForm
