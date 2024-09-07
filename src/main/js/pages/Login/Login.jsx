import { login } from '../../api/auth';
import { useState } from 'react';
import Cookies from 'js-cookie';
import {Link} from "react-router-dom";

export function Login() {
    const [formData, setFormData] = useState({email: '', password: ''});
    const [error, setError] = useState('');

    const handleInputChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await login(formData.email, formData.password);
            Cookies.set('accessToken', response.data.jwtToken);
            Cookies.set('userId', response.data.id)
            window.location.href = '/';
        } catch (error) {
            console.log(error);
            setError('Đăng nhập không thành công');
        }
    }

    return (
        <>
            <form className='flex flex-col items-center gap-y-4' onSubmit={handleSubmit}>
                <h1 className='text-2xl font-bold text-white'>Đăng nhập nè</h1>
                <input name='email' type='text' placeholder='Email' className='w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg' onChange={handleInputChange}/>
                <input name='password' type='password' placeholder='Mật khẩu' className='w-2/3 md:w-1/4 p-2 text-white caret-white bg-slate-800 rounded-lg' onChange={handleInputChange}/>
                <button className='w-2/3 md:w-1/4 p-2 bg-sky-500 text-white rounded-lg' type="submit">Đăng nhập</button>
            </form>
            <div className='text-center mt-4'>
                <Link to='/reset-password'>Quên mật khẩu?</Link>
            </div>
        </>
    );
}