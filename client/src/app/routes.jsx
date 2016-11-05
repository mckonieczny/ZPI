import React from 'react';
import {Router, Route} from 'react-router';
import App from '../App';
import Home from '../components/Home';
import {AddDeck, DecksPage, DeckViewPage} from '../features/Decks';

export default (
    <Router>
        <Route path="/" component={App}>
            <Route path="home" component={Home} />
            <Route path="addDeck" component={AddDeck} />
            <Route path="decks" component={DecksPage} />
            <Route path="deck/:deckId" component={DeckViewPage} />
        </Route>
    </Router>
);
