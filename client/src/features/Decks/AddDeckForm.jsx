import React from 'react';
import {TextField, RaisedButton} from 'material-ui';
import {Row,Col} from 'react-bootstrap';
import {browserHistory} from 'react-router';

export default class AddDeckForm extends React.Component {
    constructor () {
        super();
        this.submitForm = this.submitForm.bind(this);

        this.state = {
            name: '',
            description: ''
        };
    }

    isValid () {
        let form = this.state;
        for (let prop in form) {
            if (form[prop] === '') {
                return false;
            }
        }
        return true;
    }

    submitForm () {
        if (!this.isValid()) return;
        this.props.onSubmit(this.state);
        this.setState({
            name: '',
            description: ''
        });
        browserHistory.push('/decks');
    }


    setValue (field, event) {
        var object = {};
        object[field] = event.target.value;
        this.setState(object);
    }


    render () {

        return (
            <Row>
                <Col mdOffset={3} md={6}>
                <div>
                    <TextField
                        fullWidth
                        name="name"
                        hintText="Name"
                        value={this.state.name}
                        onChange={this.setValue.bind(this, 'name')}
                    />
                    <br />
                    <TextField
                        fullWidth
                        name="description"
                        hintText="Description"
                        multiLine
                        value={this.state.description}
                        onChange={this.setValue.bind(this, 'description')}
                    />
                    <div style={{paddingTop: '30px'}}>
                        <RaisedButton label="Create deck" fullWidth secondary onClick={this.submitForm} />
                    </div>



                </div>
                </Col>
            </Row>


        );
    }
}
