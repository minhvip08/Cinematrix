import PageTitle from "../../components/Title";
import React, {useEffect, useState} from "react";
import {formRequest} from "../../api";
import Cookies from "js-cookie";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

export function SearchContent() {
    const [queryName, setQueryName] = useState("");
    const [queryType, setQueryType] = useState("MOVIE");
    const [tmdbContentLst, setTmdbContentLst] = useState([]);

    const [fetchDone, setFetchDone] = useState(true);
    const [fireQuery, setFireQuery] = useState(false);
    const [sliceCts, setSliceCts] = useState([0, 10]);

    const showSize = 10;


    async function handleFindContent() {
        setSliceCts([0, showSize])
        setFetchDone(false);
        setFireQuery(true);
        const formData = new FormData();
        formData.append("name", queryName);
        formData.append("type", queryType);
        console.log("name:" + queryName + "-type:" + queryType)
        const urlParams = new URLSearchParams(formData);
        const reps = await formRequest.get("/content/search/external?" + urlParams);
        let data = reps.data.slice(0, 30);
        setTmdbContentLst(data);
        setFetchDone(true);
        setFireQuery(false);
        for (const contentItem of data) {
            formRequest.get("/content/check-exist/" + contentItem.id)
                .then(rs => {
                    const rsData = rs?.data;
                    const updateTmdbLst = [...data];
                    const item = updateTmdbLst.find(item => item.id === contentItem.id);
                    item.exists = true;
                    item.dbId = rsData.id;
                    setTmdbContentLst(updateTmdbLst);
                }).catch(err => {
                const updateTmdbLst = [...tmdbContentLst];
            });

        }
    }

    async function handleClickQueryAContent(tmdbItem) {
        if (tmdbItem.exists) {
            window.location.href = "/content/detail/" + tmdbItem.dbId;
        } else {
            Cookies.set("upload-content-tmdbId", tmdbItem.id);
            Cookies.set("upload-content-type", queryType);
            window.location.href = "/upload"
        }
    }

    return (
        <div>
            <PageTitle pageTitle={"Search"}/>
            <div className={"mx-auto"}>
                <div className={"flex flex-row mb-5 justify-center gap-10 items-end"}>
                    <div className="relative basis-3/4">
                        <div className="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
                            <svg className="w-4 h-4 text-gray-500 dark:text-gray-400" aria-hidden="true"
                                 xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                      stroke-width="2"
                                      d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                            </svg>
                        </div>
                        <input id="default-search" type={"text"}
                               onChange={(e) => {
                                   setQueryName(e.target.value);
                               }}
                               onKeyDown={(e) => {
                                   if (e.key === "Enter") handleFindContent()
                               }}
                               className="block w-full p-4 ps-10 text-xl font-mono font-semibold text-stone-700 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 "
                               placeholder={"Search " + (queryType === "MOVIE" ? "Movie" : "TV Show")} required/>
                        <button
                            onClick={handleFindContent}
                            className="text-white absolute end-2.5 bottom-2.5 bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-4 py-2 ">
                            Search
                        </button>
                    </div>
                    <label htmlFor="type"
                           className=" mb-2 text-2xl text-cyan-400 font-mono font-bold ">Type</label>
                    <select id="type"
                            value={queryType}
                            onChange={e => {
                                console.log(e.target.value);
                                setQueryType(e.target.value)
                            }}
                            className="  bg-gray-50 border border-gray-300 font-mono font-semibold text-gray-700 text-2xl rounded-lg focus:ring-blue-500 focus:border-blue-500 p-1 ">
                        <option value={"MOVIE"} selected>Movie</option>
                        <option value={"TV"}>TV Show</option>
                    </select>
                </div>
                <div>
                    {(tmdbContentLst.length > 0 && fetchDone) ?
                        <div className={"flex flex-row justify-evenly items-baseline"}>
                            <button onClick={() => {
                                if (sliceCts[0] > 0)
                                    setSliceCts(prevState => prevState.map(it => it - showSize));
                            }}>
                                <img width="60" height="60" src="https://img.icons8.com/badges/96/left.png" alt="left"
                                     className={"hover:scale-110 duration-150 ease-in-out"}/>
                            </button>
                            <div className={"grid grid-cols-1 sm:gri-cols:3 md:grid-cols-4 lg:grid-cols-5 gap-10"}>
                                {tmdbContentLst.slice(sliceCts[0], sliceCts[1]).map(it => {
                                    // content card div
                                    return (
                                        <a key={it.id}
                                           onClick={() => handleClickQueryAContent(it)}
                                           className={"flex flex-col justify-evenly w-48 p-2 rounded-lg overflow-hidden shadow-lg hover:shadow-cyan-200 hover:scale-105 duration-150 ease-in-out" +
                                               "cursor-pointer " + (it.exists ? "bg-blue-300 hover:bg-blue-400 " : "bg-stone-300 hover:bg-stone-200 ")}>
                                            <img src={"https://image.tmdb.org/t/p/original/" + it.poster_path}
                                                 alt={it.title || it.original_name + "poster"}
                                                 className={"w-full  rounded-lg cursor-pointer"}
                                            />
                                            <div className={"grid"}>
                                                <p className={"font-bold text-xl mb-2 text-cyan-700 font-mono cursor-pointer"}>{it.title || it.original_name}</p>
                                                <p className={"text-gray-500 font-mono text-base font-bold"}>{it.release_date}</p>
                                            </div>
                                        </a>
                                    );
                                })}
                            </div>
                            <button onClick={() => {
                                if (sliceCts[1] < tmdbContentLst.length)
                                    setSliceCts(prevState => prevState.map((it, idx) => {
                                        return it + showSize
                                    }));
                            }}>
                                <img width="60" height="60" src="https://img.icons8.com/badges/96/right.png"
                                     className={"hover:scale-110 duration-150 ease-in-out"}
                                     alt="right"/>
                            </button>
                        </div>
                        : (!fetchDone && fireQuery > 0 &&
                            <LoadingSpinner/>
                        )
                    }
                </div>
            </div>
        </div>
    );
}

