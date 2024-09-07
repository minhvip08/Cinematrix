import PageTitle from "../../components/Title";
import {useEffect, useState} from "react";
import api, {formRequest} from "../../api";
import Cookies from "js-cookie";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import {useParams} from "react-router-dom";

export function UserDetail() {
    const [fetchState, setFetchState] = useState(false);
    const [userInfor, setUserInfor] = useState({});
    const [userMsgLst, setUserMsgLst] = useState([]);


    const fetchUserInfo = async () => {
        const qrForm = new FormData();
        qrForm.append("id", Cookies.get("userId"))
        const qrUrl = new URLSearchParams(qrForm);
        try {
            //get user infor
            const getUserResp = await formRequest.get("/users/detail?" + qrUrl);
            //get user message
            const getUserMsgReps = await formRequest.get("/notify/get-notification/user?" + qrUrl);
            setUserInfor(getUserResp.data);
            setUserMsgLst(getUserMsgReps.data);
            console.log(userInfor);
            console.log(userMsgLst);
            setFetchState(true);
        } catch (e) {
            throw e;
        }
    }
    useEffect(() => {
        fetchUserInfo().then().catch(e => console.log(e.message));

    }, []);

    function handleSeenMsg(id) {
        console.log("fetch confirm read this msg");
        api.post("/notify/confirm-read/user", {
            'notiMsgLst': [id]
        })
        setUserMsgLst(userMsgLst.map(
            msg => {
                if (msg.id !== id) return msg;
                else {
                    msg.isSeen = true;
                    return msg;
                }
            }
        ))
    }

    function NotificationMsgGrid() {
        return (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {userMsgLst.map(notification => (
                    <div key={notification.id}
                         className={(notification.isSeen ? "bg-white" : "bg-cyan-300") +
                             " shadow-md rounded-lg p-4 hover:bg-white transition duration-300 ease-in-out transform hover:scale-105 cursor-pointer"}
                         onClick={() => {
                             if (!notification.isSeen) handleSeenMsg(notification.id);
                         }}
                    >
                        <h3 className="text-lg font-bold mb-2 text-gray-600">{notification.subject}</h3>
                        <p className="text-gray-500 text-xl">{notification.content}</p>
                        <p className="text-gray-400 text-sm my-4">{notification.releaseTime}</p>
                    </div>
                ))}
            </div>
        );
    }


    return (
        <>
            <PageTitle pageTitle={"Thông tin người dùng"}/>
            {!fetchState ? (
                <LoadingSpinner/>
            ) : userInfor ? (
                <>
                    <h2 className="text-4xl font-bold mb-4">{userInfor.fullName}</h2>
                    <div className="grid flex-col gap-4">
                        <div>
                            <p className="font-bold text-2xl">Email</p>
                            <p>{userInfor.email}</p>
                        </div>
                        <div>
                            <p className={"font-bold text-2xl"}>Thông báo</p>
                            <NotificationMsgGrid notifications={userMsgLst}/>
                        </div>
                        <div>
                            <p className={"font-bold text-2xl"}>Phim xem gần đây</p>
                        </div>
                    </div>
                </>
            ) : (
                <p className="text-gray-500">Không thể lấy thông tin người dùng</p>
            )}
        </>
    );
}

