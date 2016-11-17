import _ from 'lodash';
import delay from './utils/delay';

const fakeUsersCollection = {
    users: [
        {
            _id: 1,
            username: 'filip',
            password: 'abc'
        },
        {
            _id: 2,
            username: 'maciek',
            password: 'abc'
        }
    ]
}


export const login = (username, password) => 
    delay(1000).then(()=> 
        _.find(fakeUsersCollection, (user) => user.username === username && user.password === password)
    );
   
