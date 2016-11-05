import React from 'react';
import {TextField, RaisedButton} from 'material-ui';

export default class AddCardForm extends React.Component {

  constructor() {
    super();
    this.submitForm = this.submitForm.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);

    this.state = {
      word: '',
      translation: ''
    };
  }

  handleKeyDown(event) {
    if(event.keyCode == 13) {
      this.submitForm();
    }
  }

  isValid(){
    let form = this.state;
    for(let prop in form) {
      if(form[prop] === '') {
        return false;
      }
    }
    return true;
  }

  submitForm() {
    if(!this.isValid()) {
      return;
    }
    const newCard = {...this.state, deckId: this.props.deckId};
    this.props.onSubmit(newCard);
    this.setState({
      word: '',
      translation: ''
    });
  }

  setValue(field, event) {
   var object = {};
   object[field] = event.target.value;
   this.setState(object);
 }


  render() {

    return (
      <div>
          <TextField fullWidth={true}
            name="word"
            ref="wordInput"
            hintText="word"
            value={this.state.word}
            onChange={this.setValue.bind(this, 'word')}
            onKeyDown={this.handleKeyDown}
          />
          <br />
          <TextField fullWidth={true}
              name="translation"
              hintText="translation"
              value={this.state.translation}
              onChange={this.setValue.bind(this, 'translation')}
              onKeyDown={this.handleKeyDown}
          />
        <div style={{paddingTop: '30px'}}>
          <RaisedButton fullWidth={true} label="Add card" secondary={true} onClick={this.submitForm}/>
        </div>
      </div>

    );
  }
}
