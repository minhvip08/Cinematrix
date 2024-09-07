import api from '.'

export const searchUser = async (username, email) => {
    return await api.get('/users/search', {params: {username: username, email: email}});
}