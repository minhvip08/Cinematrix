import {useEffect, useRef, useState} from 'react';
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import Cookies from "js-cookie";
import {FilmIcon} from "@heroicons/react/16/solid";
import api, {formRequest} from "../../api";
import axios from "axios";

export function Upload() {
    const [registerContent, setRegisterContent] = useState(false);
    const [id, setId] = useState(null);
    const [file, setFile] = useState(null);
    const [currChunkIdx, setCurrChunkIdx] = useState(null);
    const [totalChunk, setTotalChunk] = useState(null);

    const [uploading, setUploading] = useState(false);
    const [upSuccess, setUpSuccess] = useState(false);
    const [percentProgressStr, setPercentProgressStr] = useState("0");
    const percentProgress = useRef(0);

    useEffect(() => {
        if (currChunkIdx !== null) {
            readAndUploadCurrentChunk();
        }
    }, [currChunkIdx]);

    async function readAndUploadCurrentChunk() {
        const reader = new FileReader();
        if (!file) {
            console.log("File is null, no chunk can read");
            return;
        }
        const from = currChunkIdx * presetParams.chunkSize;
        const to = Math.min(from + presetParams.chunkSize, from + file.size - currChunkIdx * presetParams.chunkSize);
        console.log("from:" + from + "-to:" + to);
        const blob = file.slice(from, to);
        try {
            const formData = new FormData();
            formData.append("chunk", blob);
            formData.append("chunkNumber", currChunkIdx);
            formData.append("id", id);
            formData.append("totalChunks", totalChunk);
            const uploadChunkResp = await formRequest.post("/upload/sv/chunk", formData);
            if (uploadChunkResp.status !== 201) {
                alert("Upload fail:" + uploadChunkResp.data);
                setUploading(false);
                setUpSuccess(true);
                return;
            }
            percentProgress.current = percentProgress.current + (1 / totalChunk * 100);
            setPercentProgressStr(percentProgress.current.toPrecision(4));
            if (currChunkIdx < totalChunk - 1)
                setCurrChunkIdx(prestate => prestate + 1);
            else {
                setUploading(false);
                setUpSuccess(true);
            }

        } catch (e) {

        }
    }

    const validateFile = (file) => {
        if (!['video/avi', 'video/mp4', 'video/x-matroska'].includes(file.type)) {
            return 'Chỉ hỗ trợ file AVI, MP4, MKV';
        }
        if (file.size > 1024 * 1024 * 1024 * 10) {
            return 'File quá lớn, tối đa 10GB';
        }
        return null;
    }

    const doSetFile = (file) => {
        const msg = validateFile(file);
        if (msg) {
            alert(msg);
        } else {
            setFile(file);
        }
    }

    async function uploadFile() {
        if (file) {
            try {
                const _totalChunk = Math.ceil(file.size / presetParams.chunkSize);
                const data = {
                    "id": id,
                    "type": 0, //MOVIE
                    "totalChunks": _totalChunk,
                }
                setTotalChunk(_totalChunk);
                const initUploadReps = await api.post("/upload/sv/init", data);
                console.log(initUploadReps.data);
                setCurrChunkIdx(0);

                setUploading(true);
            } catch (e) {
                console.log("upload fail:" + e.message);
            }

        } else {
            alert('Chưa chọn file');
        }
    }

    async function handleRegisterContent(tmdbId, type) {
        try {
            const regisResp = await api.post("/upload/register", {
                "tmdbId": tmdbId,
                "type": type
            })
            setId(regisResp.data["id"]);
            setRegisterContent(true);
        } catch (e) {
            alert("Lỗi xác nhận upload nội dung.");
            console.log("Fail to register content:" + e.message);
        }


    }
    console.log(
        "id:" + Cookies.get("upload-content-tmdbId") +
        "\nType:" + Cookies.get("upload-content-type")
    )
    function RegisterContent() {
        return (
            <>
                <p className="text-bold text-white mb-2">
                    Bạn có chắc upload nội dung có TMDB Id là: {Cookies.get("upload-content-tmdbId")} không?
                </p>
                <div className="flex items-end gap-2 flex-row-reverse">
                    <button className="p-2 bg-sky-500 text-white rounded-lg"
                            onClick={() => handleRegisterContent(Cookies.get("upload-content-tmdbId"), Cookies.get("upload-content-type"))}>
                        Xác nhận
                    </button>
                    <button className="p-2 bg-red-500 text-white rounded-lg"
                            onClick={() => {
                                window.location.href = "/";
                            }}>
                        Huỷ
                    </button>
                </div>

            </>
        );
    }

    return (
        <div className="flex justify-center my-2">
            <div className="w-1/2">
                <div className="text-2xl font-bold text-white mb-2">Upload phim</div>
                {!registerContent ? <RegisterContent/> :
                    <>
                        {(!uploading && !upSuccess) &&
                            <>
                                <p className="text-bold text-white mb-2">Chọn file phim để upload</p>
                                <label htmlFor="dropzone-file"
                                       className="flex flex-col items-center justify-center mb-2 h-64 border-2 border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600">
                                    <div className="flex flex-col items-center justify-center pt-5 pb-6 mx-5"
                                         onDragOver={(e) => {
                                             e.preventDefault()
                                         }}
                                         onDrop={(e) => {
                                             e.preventDefault();
                                             doSetFile(e.dataTransfer.files[0]);
                                         }}>
                                        {!file ?
                                            <>
                                                <svg className="w-8 h-8 mb-4 text-gray-500 dark:text-gray-400"
                                                     aria-hidden="true"
                                                     xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
                                                    <path stroke="currentColor" strokeLinecap="round"
                                                          strokeLinejoin="round"
                                                          strokeWidth="2"
                                                          d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
                                                </svg>
                                                <p className="mb-2 text-sm text-gray-500 dark:text-gray-400"><span
                                                    className="font-semibold">Click để upload</span> hoặc kéo thả</p>
                                                <p className="text-xs text-gray-500 dark:text-gray-400">AVI, MP4,
                                                    MKV</p>
                                            </>
                                            :
                                            <>
                                                <FilmIcon className="w-8 h-8 mb-4 text-gray-500 dark:text-gray-400"/>
                                                <p className="mb-2 text-sm text-gray-500 dark:text-gray-400">{file.name}</p>
                                            </>
                                        }

                                    </div>
                                    <input id="dropzone-file" type="file" className="hidden" accept=".avi,.mp4,.mkv"
                                           onChange={(e) => doSetFile(e.target.files[0])}/>
                                </label>
                                <div className="flex items-end gap-2 flex-row-reverse">
                                    <button className="p-2 bg-sky-500 text-white rounded-lg"
                                            onClick={() => uploadFile()}>Upload
                                    </button>
                                    <button className="p-2 bg-red-500 text-white rounded-lg"
                                            onClick={() => setFile(null)}>
                                        Xoá file
                                    </button>
                                </div>
                            </>
                        }
                        {
                            (uploading && !upSuccess) &&
                            <>
                                <div
                                    className="fixed -top-10 left-0 w-full h-full flex flex-col gap-4 items-center justify-center">
                                    <LoadingSpinner/>
                                    <div className="text-2xl text-white">Đang upload</div>
                                    <div className="text-2xl text-white">Tiến độ: {percentProgressStr}%</div>
                                </div>
                            </>

                        }
                        {
                            (!uploading && upSuccess) &&
                            <>
                                <div
                                    className="fixed -top-10 left-0 w-full h-full flex flex-col gap-4 items-center justify-center">
                                    <div className="text-2xl text-white">Upload thành công 😍</div>
                                    <div className="text-2xl text-white">Chúng tôi sẽ kiểm duyệt nội dung của bạn và
                                        chia sẻ nó ngay nếu hợp lệ với tiêu chuẩn.
                                    </div>
                                </div>
                            </>}
                    </>}
            </div>
        </div>
    );
}
const presetParams = {
    chunkSize: 50 * 1024 * 1024, //5MB
}