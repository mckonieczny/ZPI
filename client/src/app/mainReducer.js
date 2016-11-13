import {combineReducers} from 'redux';
import {decksReducer} from '../features/Decks/duck.js';
import {cardsReducer} from '../features/Cards/duck.js';

const mainReducer = combineReducers({
    decks: decksReducer,
    cards: cardsReducer,
});

// export const getMyDecks = (state, )

export default mainReducer;
