var rc = 1;
var firestore = firebase.firestore();
var dataContainer = [];
var table = document.getElementById('recordTable');
var tbody = document.getElementById('tableBody');
var loadBtn = document.getElementById('load');
var loadingBtn = document.getElementById('loading');
var searchKey = document.getElementById('searchKey');
var uname = document.getElementById('uname');
var uadd = document.getElementById('uadd');
var uno = document.getElementById('uno');
var uname = document.getElementById('uname');
var uemail = document.getElementById('uemail');
var uid = document.getElementById('uid');

var first = firestore.collection("DeliveryBoy")
        .orderBy('date','desc')
        .limit(10);
getSearchResult();
function getSearchResult(btn)
{
    if(btn=="load")
    {
        loadBtn.classList.add('d-none');
        loadingBtn.classList.remove('d-none');
    }
    else
    {
        loadBtn.classList.add('d-none');
        loadingBtn.classList.add('d-none');
    }
    
    first.get().then(function (documentSnapshots) {
        // Get the last visible document
        var lastVisible = documentSnapshots.docs[documentSnapshots.docs.length - 1];
        //console.log("last", lastVisible);
        if(documentSnapshots.docs.length==0)
        {
            swal("There is no Record Found");
            //alertMsg("alert-danger","There is no Record Found",2000);
            document.getElementById('spinner').classList.add('d-none');
            loadBtn.classList.add('d-none');
            loadingBtn.classList.add('d-none');
        }
        else
        {
            //dataContainer = [];
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            first = firestore.collection("DeliveryBoy")
            .orderBy('date','desc')
            .startAfter(lastVisible)
            .limit(10);
            documentSnapshots.forEach(function(doc) {
                
                dataContainer[doc.id]=doc.data();
                
                setDate(doc.id,(doc.data().date).toDate(),doc.data().name,doc.data().mobileNo,doc.data().uidPhoto);
                //doc.data().description,doc.data().category,doc.data().subCategory
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });

}
function setDate(id,tdate,name,no,idPhoto)
{   var date,day,month,year,time,hours,minutes;
    hours = tdate.getHours(); 
    minutes = tdate.getMinutes();
    year = tdate.getFullYear();
    month = ("0"+(tdate.getMonth()+1)).slice(-2);
    day = ("0"+tdate.getDate()).slice(-2);
 
    time = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;  
    minutes = minutes < 10 ? '0' + minutes : minutes;
 
    date = day+"/"+month+"/"+year+" ("+hours+":"+minutes+time+")";
 
    var field = [id,date,name,no,idPhoto];
    creatTable(field);
}
        
function creatTable(fieldList)
{   
    var row = document.createElement('TR');
    tbody.appendChild(row);
    var img = "";
    
    for(var i=0;i<7;i++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
        if(i==4)
        {
            var btnEdit = document.createElement("BUTTON");
            btnEdit.classList.add("btn","btn-info","float-right","mr-2");
            btnEdit.innerHTML="<i class='fa fa-pencil'></i>";
            btnEdit.setAttribute("id",rc+fieldList[0]);
            btnEdit.setAttribute('onclick','docEdit(this.id)');
            btnEdit.setAttribute('data-toggle','modal');
            btnEdit.setAttribute('data-target','#exampleModal');
            cell.appendChild(btnEdit);
        }
        else if(i==5)
        {
            var delbtn = document.createElement("Button");
            delbtn.classList.add("btn","btn-danger");
            delbtn.setAttribute("id",fieldList[0]);
            delbtn.setAttribute("onclick","docDelete(this,this.id)");
            var icon = document.createElement("I");
            icon.classList.add("fa","fa-trash");
            delbtn.appendChild(icon);
            cell.appendChild(delbtn);
        }
        else if(i==6)
        {
            var btn = document.createElement("Button");
            btn.classList.add("btn","btn-warning");
            btn.setAttribute("id","r"+fieldList[0]);
            btn.setAttribute("onclick","getRating(this.id)");
            //btn.setAttribute('data-toggle','modal');
            //btn.setAttribute('data-target','#ratingModel');
            var icon = document.createElement("I");
            icon.classList.add("fa","fa-star");
            btn.appendChild(icon);
            cell.appendChild(btn);
        }
    }
    //show data
    for(var j=0;j<4;j++)
    {
        if(j==3)
        {
            var link = document.createElement("a");
            link.setAttribute('target','_blank');
            link.setAttribute('href',fieldList[4]);
            link.innerHTML = "view ID";
            table.rows[rc].cells[j].appendChild(link);
        }
        else
        {
            table.rows[rc].cells[j].innerText=fieldList[j+1];
        }
    }
    rc++;
}

function docDelete(ele,docID)
{
    swal({
        title: "Are You Sure ?",
        text: "Once Delivery Boy Deleted You can't get it back!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) 
        {
            firestore.collection("DeliveryBoy").doc(docID).delete().then(function() {
                var i = ele.parentNode.parentNode.rowIndex;
                table.deleteRow(i);
                swal("Successful!", "Delivery Boy Successfully Deleted", "success");
                getPathStorageFromUrl(dataContainer[docID].uidPhoto);
            }).catch(function(error) {
                console.error("Error removing document: ", error);
            });
        } 
    });
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
var rtotal,count;
var rtbody = document.getElementById('rtbody');
function getRating(id)
{
    while(rtbody.firstChild)
    {
        rtbody.removeChild(rtbody.firstChild);
    }
    rtotal=0,count=0;
    var docid = id.slice(-20);
    firestore.collection('DeliveryBoy').doc(docid).collection('Ratting')
    .get().then((querySnapshot) => {
        if(querySnapshot.docs.length==0)
        {
            swal("Selected Delivery Boy Not yet Rated");
        }
        else
        {
            querySnapshot.forEach((doc) => {
                 rtotal += doc.data().ratting;
                 count ++; 
                 getRateUser(doc.data().userId,[(doc.data().date).toDate(),doc.data().ratting,doc.data().review]);
            });
            document.getElementById('avgrate').innerHTML = "<b>Average Rating : </b>"+(rtotal/count).toFixed(1);
            $('#ratingModel').modal('show');
        }
    });
}
function getRateUser(ruid,arr)
{ 
    firestore.collection('userInfo')
    .where('userId','==',ruid)
    .get().then(function (documentSnapshots) {
        documentSnapshots.forEach((doc) => {
            arr.push(doc.data().name);
            creatRatingTable(arr);
        });
    });
}
function creatRatingTable(obj)
{
    var row = document.createElement('TR');
    rtbody.appendChild(row);
    for(var i=0;i<4;i++)
    {
        var cell = document.createElement("TD");
        if(i==0)
        {
            var temp = obj[i].toString();
            cell.innerText = temp.slice(4,15);
        }
        else
        {
            cell.innerText = obj[i];
        }
        row.appendChild(cell);
    }
}

function docEdit(btnId)
{
    var id = btnId.slice(-20);
    
    uname.value = dataContainer[id].name;
    uadd.value = dataContainer[id].address;
    uno.value = dataContainer[id].mobileNo;
    uemail.value = dataContainer[id].email;
    uid.value = dataContainer[id].uidNo;
    
    document.getElementById('updateId').value = btnId;
}
function updateData()
{
    getKeyword(uname.value);
    var doc = document.getElementById('updateId').value;
    var id = doc.slice(-20);
    var row = doc.slice(0,(doc.length)-20);
    
    firestore.collection("DeliveryBoy").doc(id).update({
        name: uname.value,
        address: uadd.value,
        mobileNo:uno.value,
        email:uemail.value,
        uidNo:uid.value,
        keyword:arrayList
    }).then(function() {
        swal("Successful!", "Delivery Boy Details Successfully Updated!", "success")
        
        table.rows[row].cells[1].innerText=uname.value;
        table.rows[row].cells[2].innerText=uno.value;
        
        dataContainer[id].name = uname.value;
        dataContainer[id].address = uadd.value;
        dataContainer[id].mobileNo = uno.value;
        dataContainer[id].email = uemail.value;
        dataContainer[id].uidNo = uid.value;
    }).catch(function(error) {
        console.error("Error in Updating document: ", error);
    });
}

//for search
var filterFirst,subKey;
var fname,op,val;
function getFilterResult(fieldName,operator,value)
{   
    fname = fieldName;
    op = operator;
    subKey = document.getElementById('searchKey').value;
    if(value=="" && subKey=="")
    {
        swal("Enter Search Key!");
    }
    else
    {
        if(value=="")
        {
            val = subKey;
        }
        else if(value!="")
        {
            val = value;
        }
        loadingBtn.classList.add('d-none');
        loadBtn.classList.add('d-none');
        //document.getElementById('spinner').classList.remove('d-none');
        removeChild();
        filterFirst = firestore.collection("DeliveryBoy")
        //.where('keyword','array-contains',subKey)
        .where(fname,op,val)
        .limit(8);
        loadBtn.setAttribute('onclick','getFilterData(this.id)');
        getFilterData();
        
    }
}
function getFilterData(btn)
{
    if(btn=="load")
    {
        loadBtn.classList.add('d-none');
        loadingBtn.classList.remove('d-none');
    }
    else
    {
        loadBtn.classList.add('d-none');
        loadingBtn.classList.add('d-none');
    }
    filterFirst.get().then(function (documentSnapshots) {
        // Get the last visible document
        var lastVisible = documentSnapshots.docs[documentSnapshots.docs.length - 1];
        //console.log("last", lastVisible);
        if(documentSnapshots.docs.length==0)
        {
            swal("There is No Record!");
            //document.getElementById('spinner').classList.add('d-none');
            loadBtn.classList.add('d-none');
            loadingBtn.classList.add('d-none');
        }
        else
        {
            dataContainer = [];
            //document.getElementById('spinner').classList.add('d-none');
            filterFirst = firestore.collection("DeliveryBoy")
            //.where('keyword','array-contains',subKey)
            .where(fname,op,val)
            .startAfter(lastVisible)
            .limit(8);
            documentSnapshots.forEach(function(doc) {
               dataContainer[doc.id]=doc.data();
                
                setDate(doc.id,(doc.data().date).toDate(),doc.data().name,doc.data().mobileNo,doc.data().uidPhoto);
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });
}
function removeChild()
{
    var tbody = document.getElementById('tableBody');
    while(tbody.firstChild)
    {
        tbody.removeChild(tbody.firstChild);
    }
    rc=1;
}