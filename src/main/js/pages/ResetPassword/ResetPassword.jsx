import { resetPassword } from "../../api/auth";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

export function ResetPassword() {
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        setEmail(e.target.value)
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const submittedEmail = email;
            const response = resetPassword(submittedEmail);
            navigate('/verify-otp', {state: {email: submittedEmail}});
        } catch (err) {
            console.log(err);
        }
    }

    return (
        <>
            <form onSubmit={handleSubmit} className="flex flex-col items-center gap-y-4">
                <h1 className="text-2xl font-bold text-white">Quên mật khẩu</h1>
                <input name='email' type='email' placeholder='Email' onChange={handleInputChange} className="w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg" />
                <button className="w-2/3 md:w-1/4 p-2 bg-sky-500 text-white rounded-lg" type="submit">Submit</button>
            </form>
        </>
    )
}