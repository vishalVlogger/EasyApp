var imgfiles = [];
var compFile = [];
var imgurl = [] ;

document.getElementById("files").addEventListener("change", function(e) {
    var temp = document.getElementById("files");
    imgfiles = temp.files;
    for (let i = 0; i < imgfiles.length; i++) 
    {
        //console.log(imgfiles[i]);
        document.getElementById('send').classList.add('d-none');
        document.getElementById('load').classList.remove('d-none'); 
        document.getElementById('sending').classList.add('d-none');  
        //compression start
        var options = {
                maxSizeMB: 1,
                maxWidthOrHeight: 1920,
                useWebWorker: true
        }
        imageCompression(imgfiles[i], options)
        .then(function (compressedFile) {
              compFile[i]=compressedFile; // write your own logic
              if(i == (imgfiles.length)-1)
              {
                document.getElementById('send').classList.remove('d-none');
                document.getElementById('sending').classList.add('d-none');
                document.getElementById('load').classList.add('d-none');
              }
        })
        .catch(function (error) {
          console.log(error.message);
        });
        //compression end
    }
    
});


document.getElementById("send").addEventListener("click", function() {
    if (compFile.length != 0) 
    {
        //diseable button
        document.getElementById('send').disabled = true;
        document.getElementById('send').classList.add('d-none');
        document.getElementById('sending').classList.remove('d-none');
        //Loops through all the selected files
    for (let i = 0; i < compFile.length; i++) {
      //create a storage reference
      let newName = randomName(compFile[i].name);
      var storage = firebase.storage().ref(page+'/'+newName);

      //upload file compFile[i]
      var upload = storage.put(compFile[i]);
        
    

      //update progress bar
      upload.on(
        "state_changed",
        function progress(snapshot) {
    
        },

        function error() {
          console.log("error uploading file");
        },

        function complete() {
            swal(compFile[i].name+" Uploaded Successfully");
          
            //get file url
            firebase.storage().ref(page+'/'+newName)
            .getDownloadURL()
            .then(function(url) {
            imgurl[i]=url;
            console.log(imgurl[i]);
            })
            .catch(function(error) {
                  console.log(error.message);
            });//end geturl
            
            if(i == (compFile.length)-1)
            {
                document.getElementById('sending').classList.add('d-none');
                document.getElementById('send').classList.remove('d-none');
            }
        }
      );
    }
  } else {
    swal("Please First Select File");
  }
});

function randomName(name)
{
    var rand = Math.random().toString(36).slice(-5);
    var nn = rand+name.replaceAll(' ','');
    return nn;
}