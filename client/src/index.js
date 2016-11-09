import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Root from './app/Root';
import configureStore from './app/configureStore';

ReactDOM.render(
    <Root store={configureStore()} />
  , document.getElementById('root')
);
