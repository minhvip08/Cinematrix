import api, { api_directUrl } from ".";

export const fetchContentComments = async (contentId) => {
    return await api.get(`/comment/${contentId}`);
}

export const fetchCommentByUrl = async (url) => {
    return await api_directUrl.get(url);
}

export const postComment = async (targetId, targetType, content) => {
    if (typeof targetId === 'string') {
        targetId = parseInt(targetId);
    }
    console.log({targetId, targetType, content});
    return await api.post('/comment', {targetId, targetType, content});
}