export const SAVE_CARDS = 'SAVE_CARDS';
export const ADD_CARD = 'ADD_CARD';
export const DELETE_CARD = 'DELETE_CARD';
export const LOAD_CARDS = 'LOAD_CARDS';
export const LOADED_CARDS = 'LOADED_CARDS';

// @TODO add argument
export const saveCards = () => ({type: SAVE_CARDS});

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

export const deleteCard = (cardId) => ({type: DELETE_CARD, cardId});

export const loadCards = (deckId) => ({type: LOAD_CARDS, deckId});

export const loadedCards = (cards) => ({type: LOADED_CARDS, cards});
