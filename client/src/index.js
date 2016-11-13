import React from 'react';
import ReactDOM from 'react-dom';
import Root from './app/Root';
import configureStore from './app/configureStore';
import './index.css';

ReactDOM.render(
    <Root store={configureStore()} />
  , document.getElementById('root')
);
