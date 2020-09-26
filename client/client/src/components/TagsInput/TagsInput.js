import React, {useState} from 'react'
import Chip from './Chip/Chip'
import css from './TagsInput.module.less'
import Button from '../../components/Button/Button'
import * as services from '../../services/services'

const TagsInput = ({ data=[] }) => {
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

  const onSave = () => {
    services.updateProfile({
      tags: chips
    })
  }

  return (
    <div className={css.chipsBlock}>
      <div className={css.chipsContainer}>
        {chips.map((chip, i) => (
          <Chip
            key={i}
            data={chip}
            onRemove={() => onRemove(i)}
          />
        ))}
      </div>
      <input
        value={chip}
        onKeyDown={handleKeyDown}
        onChange={onChange}
      />
      <Button label='Save' onClick={onSave}/>
    </div> 
  )
}

export default TagsInput