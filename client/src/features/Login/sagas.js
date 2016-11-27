import {LOG_IN, loggedIn} from './actions';
import {call, put} from 'redux-saga/effects';
import {takeEvery} from 'redux-saga';
import {login as loginWithApi} from '../../api/user';


export function* logIn (action) {
    debugger;
    const {username, password} = action;
    const {user} = yield call(loginWithApi, username, password);
    debugger;
    yield put(loggedIn(user)); 
}

export function* watchLogIn () {
    yield* takeEvery(LOG_IN, logIn)
}

const loginSagas = [watchLogIn];
export default loginSagas;
