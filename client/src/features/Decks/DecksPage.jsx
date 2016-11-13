import React from 'react';
import {connect} from 'react-redux';
import {getUserId} from '../../api/user';
import Decks from './Decks';
import {loadDecks} from './actions';

class DecksPage extends React.Component {

    componentDidMount () {
        this.fetchDecks();
    }

    componentDidUpdate () {
        // console.log('comp did update');
        // this.fetchDecks();
    }

    fetchDecks () {
        this.props.loadDecks();
    }

    render () {
        return (
            <Decks {...this.props} />
        );
    }
}



const mapStateToProps = state => ({
    myDecks: state.decks.filter(deck => deck.ownerId === getUserId()),
    otherDecks: state.decks.filter(deck => deck.ownerId !== getUserId())
});

export default connect(mapStateToProps, {loadDecks})(DecksPage);
