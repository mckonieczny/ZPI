import React from 'react';
import DeckView from './DeckView';
import {connect} from 'react-redux';
import {getUserId} from '../../api/user';
import {deleteDeck, addToFavorites, removeFromFavorites} from './actions';
import {saveCards, loadCards} from '../Cards/actions';
import _ from 'lodash';

class DeckViewPage extends React.Component {

    componentDidMount () {
        this.props.loadCards(this.props.deck._id);
    }

    render () {
        return (
            <DeckView {...this.props} />
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    const deck = _.find(state.deck.decks, deck => deck._id.$oid === ownProps.params.deckId);
    const isOwner = getUserId() === deck.ownerId;
    return {
        deck,
        isOwner,
        isFavorite: deck.favorite,
        isFetching: state.card.isFetching
    };
};



export default connect(mapStateToProps, {
    onDelete: deleteDeck,
    onSynchronize: saveCards,
    onAddToFavorites: addToFavorites,
    onRemoveFromFavorites: removeFromFavorites,
    loadCards,
})(DeckViewPage);
