import api from ".";

export const banUser = async (userId) => {
    return await api.post('/users/ban', {userId});
}

export const unbanUser = async (userId) => {
    return await api.post('/users/unban', {userId});
}