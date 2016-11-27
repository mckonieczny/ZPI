import {LOG_IN, LOGGED_IN} from './actions';

export const userReducer = (state = {}, action) => {
    switch (action.type) {
        case LOG_IN: {
            return {...state, isLoggingIn: true};
        }
        case LOGGED_IN: {
            return {profile: action.user, isLoggingIn: false}
        }
        default: 
            return state;
    } 
};
