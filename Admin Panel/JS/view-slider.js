var firestore = firebase.firestore();
var dataContainer = [];
var spinner = document.getElementById('spinner');
getSlider();
function getSlider()
{
    firestore.collection("Slider").orderBy('date','desc').get().then((querySnapshot) => {
        querySnapshot.forEach((doc) => {
            dataContainer[doc.id] = doc.data();
            createCard(doc.id,doc.data().thumb,doc.data().flag);//,doc.data().title
        });
        spinner.classList.add('d-none');
    });
}
function createCard(id,image,fcat)
{   
    var mainDiv = document.getElementById('viewDiv');
    
    var col = document.createElement("DIV");
    col.classList.add("col-md-3","mb-3");
    col.setAttribute('id','temp'+id);
    col.style.width = "100%";
    mainDiv.appendChild(col);

    var card = document.createElement("DIV");
    card.classList.add("card");
    //card.setAttribute('id','temp'+id);
    col.appendChild(card);
    //mainDiv.appendChild(card);
    
    var imgDiv = document.createElement("DIV");
    imgDiv.classList.add("embed-responsive","embed-responsive-16by9");
    card.appendChild(imgDiv);

    var img = document.createElement("IMG");
    img.setAttribute('id','imgs'+id);
    img.setAttribute("src",image);
    img.classList.add("card-img-top","embed-responsive-item");
    imgDiv.appendChild(img);
    
    var cardBody = document.createElement("DIV");
    cardBody.setAttribute('class','card-body');
    cardBody.style.width = '100%' ;
    card.appendChild(cardBody);

    /*var h = document.createElement("P");
    h.classList.add('blogShow','card-title');
    h.setAttribute('id','h'+id);
    h.innerHTML='<b>'+title+'</b>';
    cardBody.appendChild(h);*/

    var c = document.createElement("P");
    c.classList.add('blogShow','card-text');
    c.setAttribute('id','c'+id);
    c.setAttribute('align','justify');
    c.innerHTML=fcat;
    cardBody.appendChild(c);
    
    var cardFooter = document.createElement("DIV");
    cardFooter.setAttribute('class','card-footer');
    card.appendChild(cardFooter);

    var btnDelete = document.createElement("BUTTON");
    btnDelete.classList.add("btn","btn-danger","float-right");
    btnDelete.innerHTML="<i class='fa fa-trash-o' aria-hidden='true'></i>";
    btnDelete.setAttribute('id',id);
    btnDelete.setAttribute('onclick','sliderDelete(this.id)');
    cardFooter.appendChild(btnDelete);
    
    var btnEdit = document.createElement("BUTTON");
    btnEdit.classList.add("btn","btn-info","float-right","mr-2");
    btnEdit.innerHTML="<i class='fa fa-pencil'></i>";
    btnEdit.setAttribute('id','u'+id);
    btnEdit.setAttribute('onclick','sliderEdit(this.id)');
    btnEdit.setAttribute('data-toggle','modal');
    btnEdit.setAttribute('data-target','#editModal');
    cardFooter.appendChild(btnEdit);
}

function sliderEdit(btnId)
{
    var id = btnId.slice(-20);//dataContainer vidDiv
    document.getElementById('utitle').value = dataContainer[id].title;
    document.getElementById('uvid').value = dataContainer[id].link;
    document.getElementById('cslide').innerHTML = id;
    if(dataContainer[id].flag=="Video")
    {
        document.getElementById('vidDiv').classList.remove('d-none');
    }
    else
    {
        document.getElementById('vidDiv').classList.add('d-none');
    }
}
function updateData()
{
    var uid = document.getElementById('cslide').innerHTML;
    firestore.collection("Slider").doc(uid).update({
        title: document.getElementById('utitle').value,
        link: document.getElementById('uvid').value
    }).then(function(){
        swal("Successful!", "Slider Updated Successfully", "success");
        dataContainer[uid].title = document.getElementById('utitle').value;
        dataContainer[uid].link = document.getElementById('uvid').value;
    }).catch(function(error){
        console.log(error.message);
    });
}
function sliderDelete(docID)
{
    var tempCard = document.getElementById('temp'+docID);
    swal({
        title: "Are You Sure ?",
        text: "Once Slider is Deleted You can't get it back!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) 
        {
            firestore.collection("Slider").doc(docID).delete().then(function() {
                swal("Successful!", "Slider Successfully Deleted", "success");
                getPathStorageFromUrl(document.getElementById('imgs'+docID).src);
                tempCard.remove();
            }).catch(function(error) {
                console.error("Error removing document: ", error);
            });
        } 
    });
    
    /*var result = confirm("You Want to delete this Slider");
    var tempCard = document.getElementById('temp'+docID);
    if(result==true)
    {
        firestore.collection("Slider").doc(docID).delete().then(function() {
            swal("Successful!", "Slider Successfully Deleted", "success");
            getPathStorageFromUrl(document.getElementById('imgs'+docID).src);
            tempCard.remove();
        }).catch(function(error) {
            console.error("Error removing document: ", error);
        });

    }*/
}
function getPathStorageFromUrl(url)
{
    if(url.substring(0,16)=="https://firebase")
    {
        const baseUrl = "https://firebasestorage.googleapis.com/v0/b/vdmart-4d64e.appspot.com/o/";
        let imagePath = url.replace(baseUrl,"");
        const indexOfEndPath = imagePath.indexOf("?");
        imagePath = imagePath.substring(0,indexOfEndPath);
        imagePath = imagePath.replace("%2F","/");
        deleteImage(imagePath);
    }
    else
    {
        console.log("image removed");
    }
}
function deleteImage(imgPath)
{
    var storageRef = firebase.storage();
    var desertRef = storageRef.ref(imgPath);
    desertRef.delete().then(function() {
        console.log("image Deleted");
    }).catch(function(error) {
        console.log("error:"+error);
    });
}