import _ from 'lodash';
import {v4} from 'node-uuid';

//fake API for developement 

const fakeCollection = {
    cards: [
        {
            _id: v4(),
            word: 'dom',
            translation: 'haus',
            deckId: 2,
        },
        {
            _id: v4(),
            word: 'most',
            translation: 'bridge',
            deckId: 1,
        }
    ]
};

export const fetchCards = (deckId) => _.filter(fakeCollection.cards, (card) => card.deckId === deckId);


