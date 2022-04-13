var firestore = firebase.firestore();
var title = document.getElementById('title');
var type = document.getElementById('type');
var vlink = document.getElementById('vlink');
var url = document.getElementById('url');
var fileInput = document.getElementById('imgGroup');
var textInput = document.getElementById('textInput');
var videoDiv = document.getElementById('videoDiv');
var selected = "text";

function selectInput(input)
{
    if(input)
    {
        fileInput.classList.remove('d-none');
        textInput.classList.add('d-none');
        selected = "file";
    }
    else
    {
        fileInput.classList.add('d-none');
        textInput.classList.remove('d-none');
        selected = "text";
    }
}

function videoHide(val)
{
    if(val=='Video')
    {
        videoDiv.classList.remove('d-none');
    }
    else
    {
        videoDiv.classList.add('d-none');
        vlink.value = "";
    }
}
function verifyData()
{
    if(title.value == "")
    {
        swal("Plese enter the title");
    }
    else if(type.value == "")
    {
        swal("Plese enter the type");
    }
    else if(selected == "text" && url.value=="")
    {
        swal("Plese enter the Image Url");
    }
    else if(selected == "file" && imgurl[0]==undefined)
    {
        swal("Plese Upload the Image");
    }
    else if(type.value == "Video" && vlink.value=="")
    {
        swal("please enter the video link");
    }
    else
    {
        if(selected == "text")
        {
            saveData(url.value);
        }
        else
        {
            saveData(imgurl[0]);
        }
        console.log("in else ");
    }
}

function saveData(img)
{
    firestore.collection('Slider').add({
        date: new Date(),
        title: title.value,
        flag: type.value,
        thumb: img,
        link: vlink.value
    }).then(function(){
        swal("Successful!", "Slider Saved Successfully", "success")
        .then(function (){
            document.location.reload(true);
        });
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });
}