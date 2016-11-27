export const getUserId = () => '1';

export const login = (username, password) => {
    return fetch(`http://zpi.herokuapp.com/callback?client_name=FormClient&username=${username}&password=${password}`, {
                method: 'POST',
                credentials: 'include',
            })
            .then(res => res.json())
};
