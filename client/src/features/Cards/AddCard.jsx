import {connect} from 'react-redux';
import AddCardForm from './AddCardForm';
import {addCard} from './duck';

const mapDispatchToProps = dispatch => ({
    onSubmit: (doc) => dispatch(addCard(doc))
});


export default connect(null, mapDispatchToProps)(AddCardForm);
