import React from 'react';
import {TextField, RaisedButton} from 'material-ui';
import {Col} from 'react-bootstrap';

const headerStyle = {
    fontFamily: '"Dancing Script", Georgia, Times, serif',
};


export default class SignupPage extends React.Component {

    constructor () {
        super();
        this.submitForm = this.submitForm.bind(this);
        this.registerRequest = this.registerRequest.bind(this);

        this.state = {
            name: '',
            password: ''
        };
    }

    componentWillMount () {
        document.body.style.backgroundImage = 'url(/images/background2.jpg)';
        document.body.style.backgroundSize = '100%';
        document.body.style.backgroundRepeat = 'no-repeat';
    }
    componentWillUnmount () {
        document.body.style.backgroundImage = null;
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

    registerRequest (username, password) {
        debugger;
        const payload = {
            username,
            password
        };

        // var data = new FormData();
        // data.append('json', JSON.stringify(payload));

        fetch(`http://zpi.herokuapp.com/api/register?username=${username}&password=${password}`, {
            method: 'POST',
        })
            .then((data) => {console.log(data)});
    }

    submitForm () {
        if (!this.isValid()) {
            return;
        }

        const {name, password} = this.state;
        this.registerRequest(name, password);

        this.setState({
            name: '',
            password: '',
        });

    }

    setValue (field, event) {
        var object = {};
        object[field] = event.target.value;
        this.setState(object);
    }


    render () {






        return (
            <div style={{paddingTop: '50px'}}>


                <Col md={4}>

                    <div style={headerStyle}>
                        <h1>Join Flash Learn</h1>
                    </div>
                    <TextField
                        inputStyle={{color: 'black'}}
                        name="name"
                        floatingLabelText="Username"
                        value={this.state.name}
                        onChange={this.setValue.bind(this, 'name')}
                        floatingLabelStyle={headerStyle}
                        floatingLabelFocusStyle={headerStyle}
                    />
                    <br />
                    <TextField
                        inputStyle={{color: 'black'}}
                        name="password"
                        floatingLabelText="Password"
                        type="password"
                        value={this.state.password}
                        onChange={this.setValue.bind(this, 'password')}
                        floatingLabelStyle={headerStyle}
                        floatingLabelFocusStyle={headerStyle}
                    />
                    <div style={{paddingTop: '30px'}}>
                        <RaisedButton label="Sign up" secondary onClick={this.submitForm}/>
                    </div>
                </Col>
            </div>

        );
    }
}
