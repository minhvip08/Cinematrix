import api from '.';

export const login = async (email, password) => {
    return await api.post('/auth/login', {email, password});
}

export const resetPassword = async (email) => {
    return await api.post('/auth/init_password_reset', {email})
}

export const verifyOtpAndResetPassword = async (email, otp, newPassword) => {
    return await api.post('/auth/reset_password', {email, otp, newPassword});
}