import Film from '../logo.svg?react';

export default function Logo({size}) {
    return (
        <div className='flex items-center gap-x-2'>
            <Film className={'w-auto ' + (size === 'large' ? 'h-11' : 'h-8')}/>
            <span className={'font-bold text-white ' + (size === 'large' ? 'text-5xl' : 'text-2xl')}>cinematrix</span>
        </div>
    );
}