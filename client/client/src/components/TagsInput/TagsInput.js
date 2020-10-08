import React, {useState} from 'react'
import Chip from './Chip/Chip'
import css from './TagsInput.module.less'
import Button from '../../components/Button/Button'

const TagsInput = ({ data = [], url, onSubmit }) => {
  const [chip, setChip] = useState('')
  const [chips, setChips] = useState(data)

  const handleKeyDown = (e) => {
    const spaceFreeChip = chip.replace(/ +/g, ' ').trim()

    if (e.key === 'Enter' && spaceFreeChip !== '') {
      const newChips = [...chips, spaceFreeChip]
      
      if (newChips.length < 11) {
        setChips([...chips, spaceFreeChip])
        setChip('')
      }    
    }
  }

  const onRemove = (i) => {
    setChips([...chips.slice(0, i), ...chips.slice(i + 1)])
  }

  const onChange = (e) => {
    const value = e.target.value

    if (value.length < 25) {
      setChip(value)
    }
  }

  return (
    <div className={css.chipsBlock}>
      Tags:
      {chips.length > 0 && <div className={css.chipsContainer}>
        {chips.map((chip, i) => (
          <Chip key={i}
                data={chip}
                onRemove={() => onRemove(i)}/>
        ))}
      </div>}
      <input  value={chip}
              onKeyDown={handleKeyDown}
              onChange={onChange}
              autoComplete='off'/>
      <Button label='Save' onClick={() => onSubmit(url, {tags: chips})}/>
    </div> 
  )
}

export default TagsInput