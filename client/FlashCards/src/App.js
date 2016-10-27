import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Test from '/features/test/Test';

class App extends Component {
    render () {
      return (
        <div className="App">
          <div className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <h2>React</h2>
          </div>
          <div className="App-body">
            <Test
              onClick={()=> console.log('click works')}
              counter={4}
            />
          </div>
        </div>
      );
    }
}

export default App;
