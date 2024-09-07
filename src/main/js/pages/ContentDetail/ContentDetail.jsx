import {useParams} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import api, {formRequest} from "../../api";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import PosterImage, {baseTmdbImgUrl} from "../../components/PosterImage";
import {fetchContentComments, fetchCommentByUrl, postComment} from "../../api/comments";
import {Comment} from "../../components/Comment/Comment";
import {CommentForm, CommentTypeEnum} from "../../components/Comment/CommentForm";
import fetchContent from "../../api/fetchContent";
import Cookies from "js-cookie";

export function ContentDetail() {
    const {id} = useParams()
    const [content, setContent] = useState(null);
    const [comments, setComments] = useState(null);
    const [fetchSt, setFetchSt] = useState(false);
    useEffect(() => {
        async function fetchComment() {
            try {
                const getCmResp = await fetchContentComments(id);
                setComments(getCmResp.data);
                console.log("Commend:", getCmResp.data);
            } catch (err) {
                console.log(err);
            }
        }

        fetchContent(id).then(rs => {
            console.log(rs);
            setContent(rs);
            setFetchSt(true)
        })
            .catch(e => {
                alert("Không thể lấy thông tin nội dung");
                console.error(e.message);
            });
        fetchComment();
    }, []);
    return (
        <>
            {
                fetchSt ? (
                        <div
                            className={"h-dvh"}
                            style={{
                                backgroundImage: `url(${baseTmdbImgUrl + content["backdropPath"]})`,
                                backgroundSize: 'cover',
                                boxShadow: "inset 0 0 0 2000px rgba(0,0,0,0.65)"
                            }}>
                            <div>
                                <div className={"m-5 p-5 flex flex-row justify-center gap-5"}>
                                    <div className={"w-1/3"}>
                                        <img
                                            src={"https://image.tmdb.org/t/p/original/" + content["posterPath"]}
                                            className={"mb-4 w-64 h-80 object-scale-down rounded-3xl overflow-hidden inline-flex items-center justify-center bg-transparent"}
                                            alt={"Poster content"}
                                        />
                                        {
                                            content.media.uploaded ? <button
                                                    onClick={() => {
                                                        window.location.href = '/watch/' + id;
                                                    }}
                                                    className="w-64 bg-cyan-400 hover:bg-cyan-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline">
                                                    Xem
                                                </button> :
                                                <button
                                                    onClick={() => {
                                                        Cookies.set("upload-content-tmdbId", content.tmdbId);
                                                        Cookies.set("upload-content-type", content.type);
                                                        window.location.href = "/upload"
                                                    }}
                                                    className="w-64 bg-amber-300 hover:bg-amber-400 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline">
                                                    Upload
                                                </button>
                                        }

                                    </div>
                                    <div>
                                        <h1 className={"px-5 font-bold font-heading text-4xl md:text-3xl mt-4 text-gray-300 "}>{content["title"]}</h1>
                                        <div className="mt-2 px-5 flex flex-row gap-8 flex-wrap justify-start">
                                            {content["genres"].map(
                                                (gen) => <p key={gen.name}>{gen.name}</p>
                                            )}
                                        </div>
                                        <p className={"mt-2 px-5 font-sans text-md lg:text-lg text-gray-300"}>{content["overview"]}</p>
                                        <ContentItem label={"Rating"} value={content["voteAverage"]}/>
                                        <ContentItem label={"Release Date"} value={content["releaseDate"]}/>
                                        <ContentItem label={"Duration"} value={content["runtime"]}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) :
                    <LoadingSpinner/>
            }
            <CommentForm targetId={id} targetType={CommentTypeEnum.CONTENT}/>
            <div>
                {comments != null && comments._embedded != null && (
                    comments._embedded.commentResourceList.map((item, index) => (
                        <Comment key={item.id} props={item} level={0}/>
                    ))
                )}
            </div>
        </>);
}

function ContentItem({label, value}) {
    return (
        <>
            <div className={"px-5 flex flex-row justify-begin"}>
                <p className={"mt-2 font-sans tracking-wide font-bold text-md lg:text-lg text-cyan-600 basis-1/4"}>{label}</p>
                <p className={"mt-2 font-sans tracking-wide font-bold text-md lg:text-lg text-gray-50 "}>{value}</p>
            </div>
        </>
    )
}