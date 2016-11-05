export const ADD_CARD = 'ADD_CARD';
export const DELETE_CARD = 'DELETE_CARD';

export const addCard = ({word, translation, deckId}) => {
    return {
        type: ADD_CARD,
        doc: {
            _id: Date.now(),
            word,
            translation,
            deckId,
        }
    };
};

export const deleteCard = cardId => ({type: DELETE_CARD, cardId});

const initialState = [];

export const cardsReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_CARD: {
            const newState = [...state, action.doc];
            return newState;
        }
        case DELETE_CARD: {
            return state.filter(card => card._id != action.cardId);
        }
        default:
            return state;
    }
};
