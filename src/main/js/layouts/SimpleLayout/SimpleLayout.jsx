import { Outlet } from "react-router-dom";
import { SimpleHeader } from "./SimpleHeader";

export function SimpleLayout() {
    return (
        <div className='min-h-screen flex flex-col antialiased text-slate-400 bg-slate-900'>
            <SimpleHeader />
            <div className='my-auto'>
                <Outlet />
            </div>
        </div>
    );
}