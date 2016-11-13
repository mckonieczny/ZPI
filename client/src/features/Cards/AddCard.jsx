import {connect} from 'react-redux';
import AddCardForm from './AddCardForm';
import {addCard} from './duck';

export default connect(null, {addCard})(AddCardForm);
