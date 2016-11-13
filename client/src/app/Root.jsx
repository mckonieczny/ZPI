import React from 'react';
import {Provider} from 'react-redux';
import routes from './routes';
import {Router, browserHistory} from 'react-router';

const Root = ({store}) => (
    <Provider store={store}>
        <Router history={browserHistory}>
            {routes}
        </Router>
    </Provider>
);

export default Root;
