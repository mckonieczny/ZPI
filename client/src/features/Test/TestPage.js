import Test from './Test';
import {connect} from 'react-redux';

import {increaseCounter} from './duck';

const mapDispatchToProps = dispatch => ({
    onClick: () => dispatch(increaseCounter())
});

const mapStateToProps = state => ({
    value: state.value
});



const TestPage = connect(mapStateToProps, mapDispatchToProps)(Test);
export default TestPage;
