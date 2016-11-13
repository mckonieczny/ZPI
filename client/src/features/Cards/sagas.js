import {delay, takeEvery} from 'redux-saga';
import {call, put} from 'redux-saga/effects';


export function* save () {
    yield call(delay, 2000);
    console.log('save');
}

export function* watchSave () {
    yield* takeEvery('SAVE_CARDS', save);
}

const cardSagas = [watchSave];
export default cardSagas;
