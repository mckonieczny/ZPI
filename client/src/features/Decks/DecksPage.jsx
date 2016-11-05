import Decks from './Decks';
import {connect} from 'react-redux';
import {getUserId} from '../../api/user';


const mapStateToProps = state => ({
    myDecks: state.decks.filter(deck => deck.ownerId === getUserId()),
    otherDecks: state.decks.filter(deck => deck.ownerId !== getUserId())
});

export default connect(mapStateToProps)(Decks);
