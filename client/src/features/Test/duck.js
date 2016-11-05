export const INCREASE_COUNTER = 'INCREASE_COUNTER';

export const increaseCounter = () => ({
    type: INCREASE_COUNTER,
    payload: 1,
});

const initialState = {
    value: 10
};

export const counterReducer = (state = {value: 10}, action) => {
    switch (action.type) {
        case INCREASE_COUNTER:
            return {value: state.value + action.payload};
        default:
            return state;
    }
};

