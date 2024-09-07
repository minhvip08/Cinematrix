import { useState } from "react";
import { postComment } from "../../api/comments";

export const CommentTypeEnum = {
    CONTENT: 0,
    COMMENT: 1
};

export const CommentForm = ({targetId, targetType}) => {
    const [content, setContent] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (content.trim() === '') {
            return;
        }
        try {
            const postCmResp = await postComment(targetId, targetType, content);
        }
        catch (err) {
            console.log(err);
        }
        setContent('');
    };

    const handleChange = (event) => {
        setContent(event.target.value);
    };

    return (
        <form onSubmit={handleSubmit} className="relative">
            <textarea
                value={content}
                onChange={handleChange}
                placeholder="Write your comment..."
                className="border border-gray-300 rounded-md p-2 mb-2 focus:outline-none focus:border-blue-500 w-full"
                rows={4}
            ></textarea>
            <button
                type="submit"
                className="absolute bottom-2 right-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:bg-blue-600"
            >
                Post Comment
            </button>
        </form>
    );
};