import React, {useEffect, useState} from "react";
import PageTitle from "../../components/Title";
import api from "../../api";
import PosterImage from "../../components/PosterImage";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

export function AllVerifiedTv() {
    const [contentLst, setContentLst] = useState([]);
    const [currPage, setCurrPage] = useState(0);
    const [totalPages, setTotalPages] = useState(null);
    const [fetchSt, setFetchSt] = useState(false);
    const [fetchSc, setFetchSc] = useState(false);

    async function fetchMovList(page) {
        try {
            const resp = await api.get("/content/tv/all?page=" + page);
            const rs = resp.data;
            setTotalPages(rs.page.totalPages);
            if (rs._embedded.tvList != null) {
                setContentLst(rs._embedded.tvList);
                setFetchSc(true);
            } else {
                alert("Danh sách phim rỗng");
            }
            setFetchSt(true);
        } catch (e) {
            alert("Lỗi khi tải danh sách phim");
            console.log("Fail to fetch content:" + e.message);
        }
    }

    useEffect(() => {
        fetchMovList(currPage);
    }, [currPage]);
    return (
        <>
            <PageTitle pageTitle={"TV Show"}/>
            <div className={"p-2 flex flex-row-3 justify-center gap-10 items-baseline"}>
                <button
                    className={"text-4xl font-bold text-cyan-600"}
                    onClick={() => {
                        if (currPage > 0) {
                            setFetchSc(false);
                            setFetchSt(false);
                            setCurrPage(curr => curr - 1)
                        }
                    }}>{"<"}</button>
                <div>
                    <h3 className={"text-4xl font-bold text-cyan-600"}>{currPage + 1}/{totalPages}</h3>
                </div>
                <button
                    className={"text-4xl font-bold text-cyan-600"}
                    onClick={() => {
                        if (currPage < totalPages - 1) {
                            setFetchSc(false);
                            setFetchSt(false);
                            setCurrPage(currPage => currPage + 1)
                        }
                    }}>{">"}</button>
            </div>
            <div className="grid grid-cols-5 gap-2">
                {fetchSt ? (fetchSc && contentLst.map(item => (
                        <div key={item.id}
                             className="flex flex-col gap-2 justify-center p-4 border border-cyan-300 rounded-lg shadow-md">
                            <div className={"basis-1/6"}>
                                <a href={"/content/detail/" + item["id"]}
                                   className={"text-cyan-600 hover:text-gray-300"}>
                                    <h2 className="text-xl font-semibold mb-2">{item.title}</h2>
                                </a>
                            </div>
                            <PosterImage path={item["posterPath"]}/>
                            <p className="text-gray-500 mb-2">Release Date: {item.releaseDate}</p>
                            <div className="flex flex-wrap">
                                {item.genres.map(genre => (
                                    <span key={genre.name}
                                          className="bg-gray-200 text-gray-700 px-2 py-1 rounded-full text-sm mr-2 mb-2">
                                {genre.name}
                                </span>
                                ))}
                            </div>
                        </div>
                    )))
                    : <LoadingSpinner/>}
            </div>
        </>
    );
}