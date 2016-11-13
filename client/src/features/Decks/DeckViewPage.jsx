import DeckView from './DeckView';
import {connect} from 'react-redux';
import {getUserId} from '../../api/user';
import {deleteDeck} from './actions';
import _ from 'lodash';

const mapStateToProps = (state, ownProps) => {
    const deck = _.find(state.decks, deck => deck._id == ownProps.params.deckId);
    const isOwner = getUserId() === deck.ownerId;
    return {
        deck,
        isOwner,
    };
};

const mapDispatchToProps = dispatch => ({
    onDelete: (deckId) => dispatch(deleteDeck(deckId)),
    onSynchronize: () => dispatch({type: 'SAVE_CARDS'})
});

export default connect(mapStateToProps, mapDispatchToProps)(DeckView);
