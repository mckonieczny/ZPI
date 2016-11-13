import {connect} from 'react-redux';
import AddDeckForm from './AddDeckForm';
import {createDeck} from './actions';

const mapDispatchToProps = dispatch => ({
    onSubmit: (doc) => dispatch(createDeck(doc))
});


export default connect(null, mapDispatchToProps)(AddDeckForm);
