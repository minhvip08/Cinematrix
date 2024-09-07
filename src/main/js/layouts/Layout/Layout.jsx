import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Footer } from './Footer';

export function Layout() {
  return (
    <div className='flex flex-col min-h-screen antialiased text-slate-400 bg-slate-900'>
      <Header />
      <div className='mx-8 flex-grow my-1'>
        <Outlet />
      </div>
      <Footer />
    </div>
  );
}