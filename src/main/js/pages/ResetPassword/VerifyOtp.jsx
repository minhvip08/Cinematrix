import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { verifyOtpAndResetPassword } from "../../api/auth";

// function VerifyOtp({route,navigate}) {
export function VerifyOtp() {
    const {state} = useLocation();
    const navigate = useNavigate();
    const email = state.email;

    const [otp, setOtp] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordNotMatch, setPasswordNotMatch] = useState(null);

    const handleInputChange = (e) => {
        switch (e.target.name) {
            case 'otp':
                setOtp(e.target.value);
                break;
            case 'password':
                setPassword(e.target.value);
                break;
            case 'confirmPassword':
                setConfirmPassword(e.target.value);
                break;
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setPasswordNotMatch('Confirm password does not match.');
        }
        else {
            try {
                const response = await verifyOtpAndResetPassword(email, otp, password);
                navigate('/login');
            } catch (err) {
                console.log(err);
            }
        }
    }

    return (
        <>
            <form className="flex flex-col items-center gap-y-4" onSubmit={handleSubmit}>
                <h1 className="text-2xl font-bold text-white">{`Nhập OTP cho email ${email}`}</h1>
                <input name='otp' type='text' placeholder='6-digit OTP' onChange={handleInputChange} className="w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg" />
                <input name='password' type='password' placeholder='Password' onChange={handleInputChange} className="w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg" />
                <input name='confirmPassword' type='password' placeholder='Confirm password' onChange={handleInputChange} className="w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg" />
                <button className="w-2/3 md:w-1/4 p-2 bg-sky-500 text-white rounded-lg" type="submit">Submit</button>
            </form>
            {passwordNotMatch && 
            <div className="items-center bg-red-600">
                <p className="text-white">{passwordNotMatch}</p>
            </div>}
        </>
    )
}