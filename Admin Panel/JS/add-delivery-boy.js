var firestore = firebase.firestore();
var add = document.getElementById('add');
var no = document.getElementById('no');
var email = document.getElementById('email');
var uid = document.getElementById('uid');
var Name = document.getElementById('name');

function verifyData()
{ 
    if(Name.value=="")
    {
        swal("Please Enter Name");
    }
    else if(add.value=="")
    {
        swal("Please Enter Address");
    }
    else if(no.value=="")//isNaN(no)
    {
        swal("Please Enter Mobile No");
    }
    else if(isNaN(no.value))//isNaN(no)
    {
        swal("Please Enter Mobile No in Digit only");
    }
    else if(no.value.length<10||no.value.length>10)//isNaN(no)
    {
        swal("Mobile No Must be 10 Digit");
    }
    else if(email.value=="")
    {
        swal("Please Enter Email");
    }
    else if(uid.value=="")
    {
        swal("Please Enter Aadhar No.");
    }
    else if(isNaN(uid.value))
    {
        swal("Please Enter Aadhar No. in Digit only");
    }
    else if(uid.value.length<12||uid.value.length>12)//isNaN(no)
    {
        swal("UID No Must be 12 Digit");
    }
    else
    {
        if(imgurl[0]==undefined)
        {
            swal("Please Upload Images");    
        }
        else
        {
            getKeyword(Name.value);
            saveData();
        }
    }
}
function saveData()
{
    firestore.collection('DeliveryBoy').add({
        date: new Date(),
        name: Name.value,
        address: add.value,
        mobileNo:no.value,
        email:email.value,
        uidNo:uid.value,
        uidPhoto:imgurl[0],
        keyword:arrayList
    }).then(function(){
        swal("Successful!", "Delivery Boy Added Successfully", "success")
        .then(function (){
            document.location.reload(true);
        });
    });
}