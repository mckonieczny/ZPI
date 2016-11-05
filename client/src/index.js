import React from 'react';
import ReactDOM from 'react-dom';
import {createStore, combineReducers} from 'redux';
import {Provider} from 'react-redux';
import {counterReducer} from './features/Test/duck.js';
import {decksReducer} from './features/Decks/duck.js';
import {cardsReducer} from './features/Cards/duck.js';
import {Router, browserHistory} from 'react-router';
import routes from './app/routes';
import './index.css';

const mainReducer = combineReducers({
    counter: counterReducer,
    decks: decksReducer,
    cards: cardsReducer,
});

const initialState = {
    decks: [
        {
            _id: 1,
            name: 'francuski podstawa',
            description: 'podstawowe zwroty po francusku',
            ownerId: 1
        },
        {
            _id: 2,
            name: 'niemiecki podstawa',
            description: 'podstawowe zwroty po niemiecku',
            ownerId: 2
        },
    ],
    cards: [
        {
            _id: 1,
            word: 'dom',
            translation: 'haus',
            deckId: 2,
        }
    ],
};

const store = createStore(mainReducer, initialState,
    window.devToolsExtension ? window.devToolsExtension() : undefined
);

ReactDOM.render(
    <Provider store={store}>
        <Router history={browserHistory}>
            {routes}
        </Router>
    </Provider>
  , document.getElementById('root')
);
