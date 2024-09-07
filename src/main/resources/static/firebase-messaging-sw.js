importScripts(
    'https://www.gstatic.com/firebasejs/9.18.0/firebase-app-compat.js'
);
importScripts(
    'https://www.gstatic.com/firebasejs/9.18.0/firebase-messaging-compat.js'
);

const firebaseConfig = {
    apiKey: "AIzaSyAr8ctHP15s312-gft9UqN5e9I0J1_5A6g",
    authDomain: "cinimatrix-530ac.firebaseapp.com",
    projectId: "cinimatrix-530ac",
    storageBucket: "cinimatrix-530ac.appspot.com",
    messagingSenderId: "469730973190",
    appId: "1:469730973190:web:352261753e0ecade66d587",
    measurementId: "G-E9CD9MWSC7",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function (payload) {
    console.log('Received background message ', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
    };

    self.registration.showNotification(notificationTitle,
        notificationOptions);
});



