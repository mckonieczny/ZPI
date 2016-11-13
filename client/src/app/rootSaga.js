import {deckSagas} from '../features/Decks';
import {cardSagas} from '../features/Cards';

import _ from 'lodash';

// helper functions
const joinSagas = (sagas) => _.flatten(sagas);

const runSagas = (sagas) => sagas.map(saga => saga());

const combinedSagas = joinSagas([deckSagas, cardSagas]);

export default function* rootSaga () {
    yield runSagas(combinedSagas);
}
