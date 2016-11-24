import {connect} from 'react-redux';
import AddCardForm from './AddCardForm';
import {addCard} from './actions';

export default connect(null, {addCard})(AddCardForm);
