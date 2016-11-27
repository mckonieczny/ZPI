export const LOG_IN = 'LOG_IN';
export const LOGGED_IN = 'LOGGED_IN';

export const logIn = (username, password) => ({type: LOG_IN, username, password});

export const loggedIn = (user) => ({type: LOGGED_IN, user});
