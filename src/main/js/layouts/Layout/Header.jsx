import {useEffect, useState} from 'react'
import Logo from '../../components/Logo'
import GetNotify from "../../components/GetNotify";
import Logout from "../../components/Logout";
import {Link} from "react-router-dom";

export function Header() {
    let [isOpaque, setIsOpaque] = useState(false)

    useEffect(() => {
        let offset = 50

        function onScroll() {
            if (!isOpaque && window.scrollY > offset) {
                setIsOpaque(true)
            } else if (isOpaque && window.scrollY <= offset) {
                setIsOpaque(false)
            }
        }

        onScroll()
        window.addEventListener('scroll', onScroll, {passive: true})
        return () => {
            window.removeEventListener('scroll', onScroll, {passive: true})
        }
    }, [isOpaque])

    return (
        <header
            className={
                'sticky top-0 z-40 w-full backdrop-blur flex-none transition-colors duration-500 lg:z-50 lg:border-b border-slate-50/[0.06] ' + (isOpaque
                    ? 'supports-backdrop-blur:bg-white/95 bg-slate-900/75'
                    : 'supports-backdrop-blur:bg-white/60 bg-transparent')
            }>
            <div className='max-w-8xl mx-auto'>
                <div className='py-4 border-b lg:px-8 lg:border-0 border-slate-300/10 mx-4 lg:mx-0'>
                    <div className='flex justify-between'>
                        <div className={'flex justify-start gap-4 items-end'}>
                            <Link to=''><Logo /></Link>
                            <NavBarElement href={'/content/search'} name={"Search"} />
                            <NavBarElement href={'/content/movie/all'} name={"Phim"}/>
                            <NavBarElement href={'/content/tv/all'} name={"TV"}/>
                        </div>
                        <div className={'flex justify-end gap-4 items-end'}>
                            <NavBarElement href={'/user/detail'} name={"Tôi"}/>
                            <GetNotify/>
                            <Logout />
                        </div>
                    </div>
                </div>
            </div>
        </header>
    )
}
function NavBarElement({name, href}) {
    return (<a href={href} className={'text-gray-300 text-2xl font-semibold'}>
        {name}
    </a>);
}