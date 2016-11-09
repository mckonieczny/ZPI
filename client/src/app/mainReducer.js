import {combineReducers} from 'redux';
import {counterReducer} from '../features/Test/duck.js';
import {decksReducer} from '../features/Decks/duck.js';
import {cardsReducer} from '../features/Cards/duck.js';

const mainReducer = combineReducers({
    counter: counterReducer,
    decks: decksReducer,
    cards: cardsReducer,
});

export default mainReducer;
