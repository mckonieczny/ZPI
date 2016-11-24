import {delay, throttle} from 'redux-saga';
import {call, put} from 'redux-saga/effects';
import {fetchAllDecks} from '../../api/decks';
import {LOAD_DECKS, loadedDecks} from './actions';

// to delete
export function* helloSaga () {
    yield call(delay, 2000);
    console.log('hello saga');
}

export function* loadDecks () {
    const decks = yield call(fetchAllDecks);
    yield put(loadedDecks(decks));
}

export function* createDeck () {
    // const newDeck = 
}

export function* watchLoadDecks () {
    yield* throttle(2000, LOAD_DECKS, loadDecks);
}



const deckSagas = [helloSaga, watchLoadDecks];
export default deckSagas;
