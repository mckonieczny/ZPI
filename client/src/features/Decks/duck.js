import {CREATE_DECK, DELETE_DECK, LOADED_DECKS} from './actions';
import _ from 'lodash';

const initialState = [];

export const decksReducer = (state = initialState, action) => {
    switch (action.type) {
        case CREATE_DECK: {
            const newState = [...state, action.data];
            return newState;
        }
        case DELETE_DECK: {
            return state.filter(deck => deck._id != action.deckId);
        }
        case LOADED_DECKS: {
            return _.union(state, action.decks);
        }
        default:
            return state;
    }
};
