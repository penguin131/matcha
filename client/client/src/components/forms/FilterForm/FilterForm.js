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
        const tagsString = e.tags?.replace(/\s+/g, '').trim()
        const tagsArr = tagsString.length > 0 ? tagsString.split(',') : []
        setFilters({...filters, ...e, tags: tagsArr})
      }}
      initialValues={{
        age_max: null,
        age_min: null,
        rating: null,
        tags: '',
        distance: null,
      }}
      render={({handleSubmit, submitting, pristine, values }) => (
        <form onSubmit={handleSubmit}>
          <div className={css.inputsBlock}>
            <Field  name="age_max"
                    component={NumberInput}
                    placeholder="Age max"/>
            <Field  name="age_min"
                    component={NumberInput}
                    placeholder="Age min"/>
            <Field  name="rating"
                    component={NumberInput}
                    placeholder="Min rating"/>
            <Field  name='tags'
                    component='input'
                    placeholder="Tags by commas"/>
            <Field  name="distance"
                    component={NumberInput}
                    placeholder="Min distance"/>
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
