import {delay, takeEvery} from 'redux-saga';
import {call, put} from 'redux-saga/effects';
import {LOAD_CARDS, loadedCards} from './actions';
import {fetchCards} from '../../api/cards';

export function* save () {
    // fetchCards(1).then(res => console.log(res));
    yield call(delay, 2000);
    console.log('save');
}

export function* loadCards (action) {
    const {deckId} = action;
    const cards = yield call(fetchCards, deckId);
    console.log(deckId);
    console.log(cards);
    yield call(delay, 1000);
    yield put(loadedCards(cards));
}

export function* watchSave () {
    yield* takeEvery('SAVE_CARDS', save);
}

export function* watchLoadCards () {
    yield* takeEvery(LOAD_CARDS, loadCards);
}

const cardSagas = [watchSave, watchLoadCards];
export default cardSagas;
