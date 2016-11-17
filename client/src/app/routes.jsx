import React from 'react';
import {Router, Route, browserHistory} from 'react-router';
import App from '../App';
import Home from '../components/Home';
import {LoginPage, SignupPage} from '../features/Login';
import {AddDeck, DecksPage, DeckViewPage} from '../features/Decks';

export default (
    <Router history={browserHistory}>
        <Route path="welcome" notLogged={true} component={App}  > 
            <Route path="login" component={LoginPage} />
            <Route path="signup" component={SignupPage} />
        </Route>
        <Route path="/" component={App}>
            <Route path="home" component={Home} />
            <Route path="addDeck" component={AddDeck} />
            <Route path="decks" component={DecksPage} />
            <Route path="deck/:deckId" component={DeckViewPage} />
            
        </Route>
    </Router>
);
