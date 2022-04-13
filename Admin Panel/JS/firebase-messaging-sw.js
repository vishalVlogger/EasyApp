importScripts('https://www.gstatic.com/firebasejs/7.15.5/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.15.5/firebase-messaging.js');

var firebaseConfig = {
    apiKey: "AIzaSyBg4A6VAW99vu10c_iI9X0IVYFv5UmNUgI",
    authDomain: "vdmart-4d64e.firebaseapp.com",
    projectId: "vdmart-4d64e",
    storageBucket: "vdmart-4d64e.appspot.com",
    messagingSenderId: "994829462042",
    appId: "1:994829462042:web:12f922f8586d267006ffa3",
    measurementId: "G-25TN8BLT53"
  };
firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload){
    console.log(payload);                                     
});