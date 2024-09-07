import {useEffect, useRef, useState} from "react";
import { fetchCommentByUrl } from "../../api/comments";
import { CommentForm, CommentTypeEnum } from "./CommentForm";

export const Comment = ({props, level}) => {
    const {content, createdAt, id, ownerId} = props;
    let links = [];
    // Level 1
    if (props._links?.reply) {
        links = props._links.reply;
    }
    // Level > 1
    else if (props.links) {
        links = props.links.find((item) => item.rel === 'reply');
        if (links && !Array.isArray(links)) {
            links = [links];
        }
    }
    const [replyComments, setReplyComments] = useState([]);
    const [cmtFormEnabled, setcmtFormEnabled] = useState(false);

    useEffect(() => {
        const fetchReplyComments = async () => {
            try {
                if (links) {
                    const promises = links.map(async (item) => {
                        const response = await fetchCommentByUrl(item.href);
                        // console.log(response.data);
                        return response.data;
                    });
                    const replyCommentData = await Promise.all(promises);
                    setReplyComments(replyCommentData);
                }
            } catch (err) {
                console.error(links, err);
            }
        };
        fetchReplyComments();
    }, []);

    const handleReplyClick = () => {
        setcmtFormEnabled(!cmtFormEnabled);
    }

    return (
        <>
            <div style={{ marginLeft: `${level * 2}rem` }} className={`bg-white text-black my-4 p-4`}>
                <div>
                    <span>User {ownerId}, at </span>
                    <span>{createdAt}</span>
                </div>
                <div>{content}</div>
                <button
                    onClick={handleReplyClick}
                    className="px-2 py-1 bg-gray-300 rounded-full hover:bg-gray-400 focus:outline-none float-right"
                >
                    Reply
                </button>
                {cmtFormEnabled ? (
                    <CommentForm targetId={id} targetType={CommentTypeEnum.COMMENT}/>
                ) : (
                    <></>
                )}
            </div>
            {replyComments.map((item, index) => (
                <Comment key={item.id} props={item} level={level + 1} />
            ))}
        </>

    )
}