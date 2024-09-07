import React, {useEffect, useState} from "react";
import PageTitle from "../../components/Title";
import api, {formRequest} from "../../api";

export function AdminPage() {
    const [contentLst, setContentLst] = useState([]);
    const [fetchSt, setFetchSt] = useState(false);
    async function fetchAllContent() {
        try {
            const fetchCtResp = await api.get("/content/all");
            setContentLst(fetchCtResp.data);
            setFetchSt(true);
        } catch (e) {
            console.log("Fail to fetch content:" + e.message)
        }
    }
    useEffect(() => {
        fetchAllContent();
    }, []);

    function handleToggleVerified(id) {
        const formData = new FormData();
        formData.append("contentId", id);
        const urlParams = new URLSearchParams(formData);
        const contentIt = contentLst.find(ct => ct.id === id);
        console.log(contentIt)
        console.log("Set verified of content " + contentIt.id + " in state " + contentIt.verified);
        if (!contentIt.verified)
            formRequest.get("/content/verify?" + urlParams).then((rs) => {
                console.log("Verification success:" + rs.data);
            })
                .catch((er) => {
                    console.log("Verification fail:" + er.message);
                });
        else {
            formRequest.get("/content/remove-verified?" + urlParams).then((rs) => {
                console.log("Verification success:" + rs.data);
            })
                .catch((er) => {
                    console.log("Verification fail:" + er.message);
                });
        }
        setContentLst(contentLst.map(
            ct => {
                if (ct.id !== id) return ct;
                else {
                    ct.verified = !ct.verified;
                    return ct;
                }
            }
        ))
    }

    function handleDeleteContent(id) {
        const resp = api.post("/content/delete", {"id": id.toString()})
            .then((rs) => {
                console.log("Delete success")
                fetchAllContent();
            })
            .catch(err => {
                console.log("Delete fail: " + err.message)
            });
    }

    return (
        <>
            <PageTitle pageTitle={"Trang Admin"}/>
            <div className="grid grid-cols-2 gap-4">
                {
                    fetchSt ? (contentLst.map(item => (
                        <div key={item.id} className="p-4 border border-gray-300 rounded-lg shadow-md">
                            <a href={"/content-detail/" + item["id"] }>
                            <h2 className="text-xl font-semibold mb-2">{item.title}</h2>
                            </a>
                            <p className="text-gray-700 mb-2">{item.overview}</p>
                            <p className="text-gray-500 mb-2">Release Date: {item.releaseDate}</p>
                            <div className="flex flex-wrap">
                                {item.genres.map(genre => (
                                    <span key={genre.name}
                                          className="bg-gray-200 text-gray-700 px-2 py-1 rounded-full text-sm mr-2 mb-2">
                                {genre.name}
                              </span>
                                ))}
                            </div>
                            <div className={"flex gap-10 items-baseline"}>
                                <label className={"text-gray-50 mx-4"} htmlFor={`verified-${item.id}`}>
                                    Đã kiểm duyệt
                                </label>
                                <input type={"checkbox"}
                                       id={`verified-${item["id"]}`}
                                       checked={item.verified}
                                       onChange={() => {
                                           handleToggleVerified(item.id);
                                       }}
                                       className={"checkboxes h-5 w-5 text-indigo-600 checked:bg-cyan-300"}
                                />
                                <button onClick={() => {
                                    handleDeleteContent(item["id"]);
                                }}
                                        className="inline-flex items-center justify-center w-32 h-8 rounded-full bg-red-500 hover:bg-red-600
                                        focus:outline-none focus:ring-2 focus:ring-red-500 text-white transition duration-300 ease-in-out"
                                >Delete
                                </button>
                            </div>
                        </div>
                    ))) : <p>Data has not been fetched yet</p>
                }
            </div>
        </>
    );
}