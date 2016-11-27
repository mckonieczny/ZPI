import {combineReducers} from 'redux';
import {decksReducer} from '../features/Decks/duck.js';
import {cardsReducer} from '../features/Cards/duck.js';
import {userReducer} from '../features/Login/duck.js';

const mainReducer = combineReducers({
    deck: decksReducer,
    card: cardsReducer,
    user: userReducer,
});

// export const getMyDecks = (state, )

export default mainReducer;
