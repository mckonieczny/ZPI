import {CREATE_DECK, DELETE_DECK, LOADED_DECKS, LOAD_DECKS, ADD_TO_FAVORITES, REMOVE_FROM_FAVORITES} from './actions';
import _ from 'lodash';
import {combineReducers} from 'redux';


export const decks = (state = [], action) => {
    switch (action.type) {
        case CREATE_DECK: {
            const newState = [...state, action.data];
            return newState;
        }
        case DELETE_DECK: {
            return state.filter(deck => deck._id !== action.deckId);
        }
        case LOADED_DECKS: {
            return _.union(state, action.decks);
        }
        case ADD_TO_FAVORITES: {
            return state.map((deck) => {
                if (deck._id === action.deckId) {
                    return {...deck, favorite: true};
                }
                return deck;
            })
        }
        case REMOVE_FROM_FAVORITES: {
            return state.map((deck) => {
                if (deck._id === action.deckId) {
                    return {...deck, favorite: false};
                }
                return deck;
            }) 
        }
        default:
            return state;
    }
};

export const isFetching = (state = true, action) => {
    switch (action.type) {
        case LOAD_DECKS: {
            return true;
        }
        case LOADED_DECKS: {
            return false;
        }
        default:
            return state;
    }
};

export const decksReducer = combineReducers({
    decks,
    isFetching
});
