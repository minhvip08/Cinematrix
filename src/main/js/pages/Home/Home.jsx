    import EmblaCarousel from "../../components/EmblaCarousel/EmblaCarousel";
import {useEffect, useState} from "react";
import {getClientToken} from "../../firebase";
import GetNotify from "../../components/GetNotify";

export function Home() {
    const OPTIONS = { align: 'start' }
    const SLIDE_COUNT = 6
    const SLIDES = Array.from(Array(SLIDE_COUNT).keys())

    //assign device token to backend
    const [token, setTokenFound] = useState(false);
    useEffect(() => {
        getClientToken(setTokenFound).then();
    }, []);

    return (
        <div className='mx-4 sm:mx-6 md:mx-8 my-2 sm:my-3 md:my-4'>
            <EmblaCarousel slides={SLIDES} options={OPTIONS} title={'Phim cập nhật gần đây'} />
        </div>
    );
}