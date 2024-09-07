import React, {useEffect, useState} from "react";
import PageTitle from "../../components/Title";
import api from "../../api";
import {baseTmdbImgUrl} from "../../components/PosterImage";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

export function AllVerifiedMovie() {
    const [contentLst, setContentLst] = useState([]);
    const [currPage, setCurrPage] = useState(0);
    const [totalPages, setTotalPages] = useState(null);
    const [fetchSt, setFetchSt] = useState(false);
    const [fetchSc, setFetchSc] = useState(false);

    async function fetchMovList(page) {
        try {
            const resp = await api.get("/content/movie/all?page=" + page);
            const rs = resp.data;
            setTotalPages(rs.page.totalPages);
            if (rs._embedded.movieList != null) {
                setContentLst(rs._embedded.movieList);
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
            <PageTitle pageTitle={"Phim"}/>
            <div className={"mb-4 p-2 flex flex-row-3 justify-center gap-10 items-cener "}>
                <button onClick={() => {
                        if (currPage > 0) {
                            setFetchSc(false);
                            setFetchSt(false);
                            setCurrPage(curr => curr - 1)
                        }
                    }}>
                    <img width="40" height="40" src="https://img.icons8.com/badges/96/left.png" alt="left"
                            className={"hover:scale-110 duration-150 ease-in-out"}/>
                </button>
                <div>
                    <h3 className={"text-4xl font-mono font-bold text-stone-100"}>{currPage + 1}/{totalPages}</h3>
                </div>
                <button onClick={() => {
                        if (currPage < totalPages - 1) {
                            setFetchSc(false);
                            setFetchSt(false);
                            setCurrPage(currPage => currPage + 1)
                        }
                    }}>
                    <img width="40" height="40" src="https://img.icons8.com/badges/96/right.png"
                         className={"hover:scale-110 duration-150 ease-in-out"}
                         alt="right"/>
                </button>
            </div>
            <div className="grid grid-cols-1 sm:gri-cols:3 md:grid-cols-4 lg:grid-cols-5 gap-10">
                {fetchSt ? (fetchSc && contentLst.map(item => (
                        <div key={item.id}
                             className="flex flex-col justify-around w-48 p-2 rounded-lg bg-stone-300 hover:bg-stone-200 overflow-hidden shadow-lg hover:shadow-cyan-200 hover:scale-105 duration-150 ease-in-out cursor-pointer">
                            <img src={baseTmdbImgUrl + item["posterPath"]} alt={item.title + " poster"}
                                 className={"w-full rounded-lg cursor-pointer"}/>
                            <div >
                                <a href={"/content/detail/" + item["id"]}
                                   className={"text-cyan-600 font-mono font-bold hover:text-cyan-800 "}>
                                    <h2 className="text-xl font-semibold mb-2">{item.title}</h2>
                                </a>
                            </div>
                            <p className="text-gray-500 mb-2">Release Date: {item.releaseDate}</p>
                            <div className="flex flex-wrap ">
                                {item.genres.map(genre => (
                                    <span key={genre.name}
                                          className="bg-gradient-to-r from-cyan-400 to-indigo-400 text-white font-mono font-bold px-2 py-1 rounded-full text-sm mr-2 mb-2">
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