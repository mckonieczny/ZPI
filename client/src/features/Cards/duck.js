import {combineReducers} from 'redux';
import {ADD_CARD, DELETE_CARD, LOAD_CARDS, LOADED_CARDS} from './actions';
import _ from 'lodash';

export const cards = (state = [], action) => {
    switch (action.type) {
        case ADD_CARD: {
            const newState = [...state, action.doc];
            return newState;
        }
        case DELETE_CARD: {
            return state.filter(card => card._id != action.cardId);
        }
        case LOADED_CARDS: {
            return _.union(state, action.cards);
        }
        default:
            return state;
    }
};

export const isFetching = (state = true, action) => {
    switch (action.type) {
        case LOAD_CARDS: {
            return true;
        }
        case LOADED_CARDS: {
            return false;
        }
        default:
            return state;
    }
};

export const cardsReducer = combineReducers({
    cards,
    isFetching
});
