const messaging = firebase.messaging();
var database = firebase.database();

function initilizeNotificatio()
{
    if(Notification.permission=='granted')
    {
        console.log("token saved already in database")
    }
    else
    {
        messaging.requestPermission()
          .then(function() {
              console.log('Notification permission granted.');
              return messaging.getToken();
          })
          .then(function (token){
            saveToken(token);
            console.log(token);
          })
          .catch(function(err) {
           console.log('Unable to get permission to notify.', err);
          });
    }
}

messaging.onMessage(function (payload){
    console.log(payload);
});

messaging.onTokenRefresh(function(){
    messaging.getToken()
    .then(function (newToken){
        saveToken(newToken);
        console.log("new Token:"+newToken);
    })
    .catch(function(err) {
       console.log(err);
    });
});

initilizeNotificatio();

function saveToken(tk)
{
    database.ref('TokenDetails').push({
        email: localStorage.getItem('logEmail'),
        token: tk
    });
}