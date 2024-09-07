import {Link} from "react-router-dom";

export default function NavBar({ paths }) {
    return (
        <div className='flex gap-x-4 items-center text-white'>
            {paths.map((path, index) => {
                return (
                    <Link key={index} to={path.href} className="hover:text-sky-500">{path.title}</Link>
                );
            })}
        </div>
    )
}