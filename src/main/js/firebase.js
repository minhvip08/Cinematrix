// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import api from "./api";
import Cookies from "js-cookie";
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyAr8ctHP15s312-gft9UqN5e9I0J1_5A6g",
    authDomain: "cinimatrix-530ac.firebaseapp.com",
    projectId: "cinimatrix-530ac",
    storageBucket: "cinimatrix-530ac.appspot.com",
    messagingSenderId: "469730973190",
    appId: "1:469730973190:web:352261753e0ecade66d587",
    measurementId: "G-E9CD9MWSC7"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const messaging = getMessaging();
export const getClientToken = async (setTokenfound) => {
    return getToken(messaging, {vapidKey: "BEVM_5RWp9MZqseezE5GfupQGjTd3Ae_JsiGLAiZJYsZkYJtmY9BBH_2hBaOqIwVC2WWjKT0i4eEUSV8DZZhSoo"}).then((currentToken) => {
        if (currentToken) {
            console.log("current token for client: ", currentToken);
            setTokenfound(true);
            //Track the token -> client mapping, by sinding to backend server
            //TODO: make endpoint for register user device
            api.post("/users/register/device-token", {
                "userId": Cookies.get("userId"),
                "deviceToken": currentToken,
            }).then((rs) => {console.log(rs)}).catch(e =>
            console.log("register device token fail:" + e.message));
            //show on the UI that permission is secured
        } else {
            console.log("No registration token available. Request permission to generate one.");
            setTokenfound(false);
            //show on the UI that permission is required
        }
    }).catch((err) => {
        console.log("An error occured while reitriving token. ", err);
    })
}

export const onMessageListener = () =>
    new Promise((resolve) => {
        onMessage(messaging, (payload) => {
            resolve(payload);
        });
    });