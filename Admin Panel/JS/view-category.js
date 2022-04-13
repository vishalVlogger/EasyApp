var fetchData,type;
var firestore = firebase.firestore();
//var sub = document.getElementById('sub');
var mainCat = document.getElementById('mainCat');
var subCat = document.getElementById('subCat');
var pin = document.getElementById('pin');
var mainDiv = document.getElementById('mainDiv');
var subCatDiv = document.getElementById('subCatDiv');
var pinDiv = document.getElementById('pinDiv');
fetchData();
function getSelectValue(val)
{
    if(val=="Main Category")
    {
        mainDiv.classList.remove('d-none');
        //subDiv.classList.add('d-none');
        subCatDiv.classList.add('d-none');
        pinDiv.classList.add('d-none');
        mainCat.setAttribute('onchange','');
        setOptions("mainCategory",mainCat);
        type = val;
    }
    else if(val=="Sub Category")
    {
        //subDiv.classList.add('d-none');
        mainDiv.classList.remove('d-none');
        subCatDiv.classList.remove('d-none');
        pinDiv.classList.add('d-none');
        setOptions("mainCategory",mainCat);
        mainCat.setAttribute('onchange','setSubOption(this.value)');
        type = val;
    }
    else
    {
        //subDiv.classList.add('d-none');
        mainDiv.classList.add('d-none');
        subCatDiv.classList.add('d-none');
        pinDiv.classList.remove('d-none');
        setOptions("pinCode",pin);
        type = val;
    }
}
function getData()
{ 
    
    if(type == "Main Category")
    {
        if(mainCat.value=="")
        {
            swal("Please Select Main Category");
            //alertMsg("alert-danger","Please Select Main Category",2000);
        }
        else
        {
            saveData("mainCategory",mainCat.value,"Main Category");
        }
    }
    else if(type == "Pin Code")
    {
        if(pin.value=="")
        {
            swal("Please Select Pin Code");
            //alertMsg("alert-danger","Please Select Main Category",2000);
        }
        else
        {
            saveData("pinCode",pin.value,"Pin Code");
        }
    }
    else if(type == "Sub Category")
    {
        if(mainCat.value=="")
        {
            swal("Please Select Main Category");
            //alertMsg("alert-danger","Please Select Main Category",2000);
        }
        else
        {
            var len = subCat.options.length;
            var setArray = [];
            //console.log(len);

            for(var i=0;i<len;i++)
            {
                if(subCat.options[i].selected==false)
                {
                    setArray.push(subCat.options[i].value);
                    //console.log(temp.options[i].value);
                }
            }
            setArray.shift();
            saveSubData(mainCat.value,setArray)
        }
        
        //console.log(setArray);
    }
    else
    {
        swal("Please Select What you Want Delete");
        //alertMsg("alert-danger","Please Select What you Want Delete",2000);
    }
}

function fetchData()
{
    firestore.collection('Content').doc('Categories').get().then(function(doc) {
        if (doc.exists) {
            fetchData = doc.data();
        } else {
            console.log("No such document!");
        }
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });
}

function setOptions(name,ele)
{
    console.log("in set");
    while (ele.options.length!=1) 
    {
      ele.remove(1);
        console.log("in remove element");
    }
    if (fetchData[name]) 
    {
        for (var i = 0; i <fetchData[name].length; i++) 
        {
              var item = new Option(fetchData[name][i]);
              ele.options.add(item);
        }
    }
}
function setSubOption(val)
{
    console.log("in sub fun");
    setOptions(val,subCat);
}
function saveData(field,val,text)
{
    swal({
        title: "Are You Sure ?",
        text: "Once "+text+" Deleted You can't get it back!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) 
        {
            firestore.collection('Content').doc('Categories').update({
                [field]: firebase.firestore.FieldValue.arrayRemove(val)
            }).then(function(){
                swal("Successful!", text+" Deleted Successfully", "success")
                .then(function (){
                    document.location.reload(true);
                });
            });
        } 
    });
}
function saveSubData(field,val)
{
    firestore.collection('Content').doc('Categories').set({
        [field]: val
    },{merge:true}).then(function(){
        swal("Successful!", "Sub Category Deleted Successfully", "success")
        .then(function (){
            document.location.reload(true);
        });
        //alertMsg("alert-success","Sub Category Delete successfully",2000);
        //document.location.reload(true);
    });
}