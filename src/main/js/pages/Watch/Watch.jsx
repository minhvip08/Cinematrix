import {useParams} from "react-router-dom";
import VideoJS from "../../components/VideoJS/VideoJS";
import PageTitle from "../../components/Title";
import {useEffect, useState} from "react";
import fetchContent from "../../api/fetchContent";

export function Watch() {
    const [content, setContent] = useState(null);
    const [fetchSt, setFetchSt] = useState(false);
    const {id} = useParams()
    useEffect(() => {
        fetchContent(id).then(rs => {
            setContent(rs);
            setFetchSt(true);
        })
            .catch(e => {
                alert("Không thể lấy thông tin nội dung");
                console.error(e.message);
            });
    }, []);

    const options = {
        controls: true,
        sources: [{
            src: id === 'sample' ? 'https://cdn.cinematrix.fun/file/cinematrix/sample/stream.mpd' :
                'https://cdn.cinematrix.fun/file/cinematrix/' + id + '/stream.mpd',
            type: 'application/dash+xml'
        }],
        // tracks: [{
        //     src: id === 'sample' ? '/sample/subtitles.vtt' : '../sample/messi.srt',
        //     kind: 'subtitles',
        //     srclang: 'en',
        //     label: 'English'
        // }]
    };

    return (<>
        {fetchSt &&
            <>
                <PageTitle pageTitle={content.title}/>
                <VideoJS options={options}/>
            </>}
    </>);
}