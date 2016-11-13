import {getUserId} from '../../api/user';

export const CREATE_DECK = 'CREATE_DECK';
export const DELETE_DECK = 'DELETE_DECK';
export const LOAD_DECKS = 'LOAD_DECKS';
export const LOADED_DECKS = 'LOADED_DECKS';


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

export const loadDecks = () => ({type: LOAD_DECKS});

export const loadedDecks = (decks) => ({type: LOADED_DECKS, decks});
