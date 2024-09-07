import axios from 'axios';

const baseUrl = `${import.meta.env.VITE_ORIGIN}/api`

const api = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
});

export const formRequest = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
    headers: {
        'Content-Type': 'multipart/form-data',
    }
});
export const api_directUrl = axios.create({
    baseUrl: '',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
})

export default api;