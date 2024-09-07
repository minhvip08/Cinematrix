import {useLocation} from 'react-router-dom';
import Logo from '../../components/Logo';
import NavBar from '../../components/NavBar/NavBar';
import './simple.css';

export function SimpleHeader() {
  const location = useLocation();
  return (
    <header className='top-0 w-full'>
      <div className='max-w-8xl mx-auto'>
        <div className='py-4 lg:px-8 mx-4 lg:mx-0'>
          <div className='relative flex justify-between items-center'>
            {/*Do not use Link here*/}
            <a href=''><Logo /></a>
            {location.pathname === '/login' ? 
            <NavBar paths={[{href: '/register', title: 'Đăng ký'}]} /> :
            location.pathname === '/register' ?
            <NavBar paths={[{href: '/login', title: 'Đăng nhập'}]} /> :
            location.pathname === '/reset-password' ?
            <NavBar paths={[{href: '/login', title: 'Đăng nhập'}, {href: '/register', title: 'Đăng ký'}]} /> :
            null}
          </div>
        </div>
      </div>
    </header>
  )
}