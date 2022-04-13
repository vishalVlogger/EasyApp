var firestore = firebase.firestore();
firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        setTimeout(function(){ window.location.href = "add-product.html"; }, 1000);
    }
});
var load = document.getElementById('load');
var loading = document.getElementById('loading');

var userEmail,userPass;
function verifyData()
{
    userEmail = document.getElementById('userEmail').value;
    userPass = document.getElementById('userPass').value;
    
    if(userEmail=="")
    {
        swal("Email Not Be Blank!");
        
    }
    else if(userPass=="")
    {
       swal("Password Not Be Blank!"); 
    }
    else
    {   
        if(ValidateEmail(userEmail))
        {
            load.classList.add('d-none');
            loading.classList.remove('d-none');
            firebase.auth().signInWithEmailAndPassword(userEmail, userPass)
             .then(function(){
                 document.getElementById('userEmail').value = "";
                 document.getElementById('userPass').value = "";
                
                 firebase.firestore().collection("Admin").where("email", "==", userEmail).get().then(function(querySnapshot){
                     querySnapshot.forEach(function(doc){
                         localStorage.setItem("userName", doc.data().name);
                         //window.location.replace = "add_question.html";
                         setTimeout(function(){ window.location.href = "add-product.html"; }, 3000);
                     })
                 });
             })
             .catch(function(error) {
              // Handle Errors here.
              var errorCode = error.code;
              var errorMessage = error.message;
              swal("Error",errorMessage,'error');
              load.classList.remove('d-none');
              loading.classList.add('d-none');
            });
        }
        else
        {
            swal("Enter Valid Email!");
        }
    }
}
var developerInfo = {
    "Name": "Lalit Jagdish Vispute",
    "Contact": "8329040477",
    "Visit": "https://m.facebook.com/lalitsoftcreation",
}
function ValidateEmail(inputText) {
    var mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if (inputText.match(mailformat)) {
        //alert("Valid email address!");
        return true;
    } else {
         
        return false;
    }
}

