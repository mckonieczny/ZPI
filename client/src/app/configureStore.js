import {createStore} from 'redux';
import mainReducer from './mainReducer';

const initialState = {
    decks: [
        {
            _id: 1,
            name: 'francuski podstawa',
            description: 'podstawowe zwroty po francusku',
            ownerId: 1
        },
        {
            _id: 2,
            name: 'niemiecki podstawa',
            description: 'podstawowe zwroty po niemiecku',
            ownerId: 2
        },
    ],
    cards: [
        {
            _id: 1,
            word: 'dom',
            translation: 'haus',
            deckId: 2,
        }
    ],
};

const configureStore = () => {
    const store = createStore(
        mainReducer,
        initialState,
        window.devToolsExtension ? window.devToolsExtension() : undefined
    );
    return store;
};

export default configureStore;
