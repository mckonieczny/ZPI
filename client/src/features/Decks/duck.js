import {getUserId} from '../../api/user';

export const CREATE_DECK = 'CREATE_DECK';
export const DELETE_DECK = 'DELETE_DECK';

export const createDeck = ({name, description}) => {
    const ownerId = getUserId();
    return {
        type: CREATE_DECK,
        data: {
            _id: Date.now(),
            name,
            description,
            ownerId,
        }
    };
};

export const deleteDeck = (deckId) => ({type: DELETE_DECK, deckId});

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

        default:
            return state;
    }
};
