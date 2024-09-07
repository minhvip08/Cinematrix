import api, {formRequest} from ".";

export default async function fetchContent(id) {
    try {
        const getCtResp = await api.get("/content/detail/" + id);
        console.log(getCtResp.data);
        return getCtResp.data;
    } catch (e) {
        throw e;
    }
}