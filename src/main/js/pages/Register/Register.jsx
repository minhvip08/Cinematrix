export function Register() {

    return (
        <form className='flex flex-col items-center gap-y-4'>
            <h1 className='text-2xl font-bold text-white'>Đăng ký</h1>
            <input type='email' placeholder='Email' className='w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg' />
            <input type='password' placeholder='Mật khẩu' className='w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg' />
            <input type='password' placeholder='Nhập lại mật khẩu' className='w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg' />
            <input type='text' placeholder="Invite code" className='w-2/3 md:w-1/4 p-2 text-white bg-slate-800 rounded-lg' />
            <button className='w-2/3 md:w-1/4 p-2 bg-sky-500 text-white rounded-lg' type="submit">Đăng ký</button>
        </form>
    );
}