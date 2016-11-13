import {createStore, applyMiddleware} from 'redux';
import mainReducer from './mainReducer';
import createSagaMiddleware from 'redux-saga';
import rootSaga from './rootSaga';

const configureStore = () => {
    const sagaMiddleware = createSagaMiddleware();
    const store = createStore(
        mainReducer,
        window.devToolsExtension ? window.devToolsExtension() : undefined,
        applyMiddleware(sagaMiddleware)
    );
    sagaMiddleware.run(rootSaga);
    return store;
};

export default configureStore;
