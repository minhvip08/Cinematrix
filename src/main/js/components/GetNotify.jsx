import {useState} from 'react';
import {onMessageListener} from "../firebase";
import {toast, ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css"

export default function GetNotify() {
    const [notification, setNotification] = useState({title: "No message", body: ''});
    const [cfGetNoti, setCfGetNoti] = useState(true);

    //enable pop up for asking for permission for send notification
    onMessageListener().then(payload => {
        setNotification({title: payload.notification.title, body: payload.notification.body})
        showToastMessage(payload.notification.subject, "info");
        setCfGetNoti(false);
        console.log(payload);
    }).catch(err => console.log('failed: ', err));


    const showToastMessage = (msg, toastType) => {
        let ops = {position: "top-right", icon: false, autoClose: 2000, pauseOnHover: true};
        if (toastType === "success")
            toast.success(msg, ops);
        else
            toast.info(msg, ops);
    };
    return (
        <>
                <button onClick={() => {
                    showToastMessage(notification.title + ":\n" + notification.body, "info")
                    setCfGetNoti(true);
                }}
                    className={(cfGetNoti ? "bg-cyan-300" : "bg-amber-300") +
                    " hover:bg-cyan-500 text-white font-extrabold py-2 px-4 rounded-lg"}
                >Thông báo</button>
                <ToastContainer/>
        </>
    );
}
