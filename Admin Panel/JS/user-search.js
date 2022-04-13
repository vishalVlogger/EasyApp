var firestore = firebase.firestore();
var table = document.getElementById('recordTable');
var tbody = document.getElementById('tableBody');
var loadBtn = document.getElementById('load');
var loadingBtn = document.getElementById('loading');
var rc = 0;

first = firestore.collection("SerchQuerys")
.orderBy('date')
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
            document.getElementById('spinner').classList.add('d-none');
            loadBtn.classList.add('d-none');
            loadingBtn.classList.add('d-none');
        }
        else
        {
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            first = firestore.collection("SerchQuerys")
            .orderBy('date')
            .startAfter(lastVisible)
            .limit(10);
            documentSnapshots.forEach(function(doc) {
                rc++;
                creatTable([rc,setDateObj((doc.data().date).toDate()),doc.data().tag,doc.data().userID]);
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });

}
function creatTable(fieldList)
{
    var row = document.createElement('TR');
    tbody.appendChild(row);
    
    for(var i=0;i<4;i++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
        if(i==3)
        {
            var btnView = document.createElement("BUTTON");
            btnView.classList.add("btn","btn-info");
            btnView.innerHTML="<i class='fa fa-eye'></i>";
            btnView.setAttribute('id',fieldList[3]);
            btnView.setAttribute('onclick','viewUser(this.id)');
            btnView.setAttribute('data-toggle','modal');
            btnView.setAttribute('data-target','#viewDetails');
            cell.appendChild(btnView)
        }
        else
        {
            cell.innerText = fieldList[i];
        }   
    }
}
function setDateObj(tdate)
{
    var day,month,year,time,hours,minutes;
    hours = tdate.getHours(); 
    minutes = tdate.getMinutes();
    year = tdate.getFullYear();
    month = ("0"+(tdate.getMonth()+1)).slice(-2);
    day = ("0"+tdate.getDate()).slice(-2);
 
    time = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;  
    minutes = minutes < 10 ? '0' + minutes : minutes;
 
    return day+"/"+month+"/"+year+" ("+hours+":"+minutes+time+")";
}
function viewUser(uid)
{
    if(uid == "notLogin")
    {
        document.getElementById("name").innerHTML = "Unregister User";
        document.getElementById("add").innerHTML = "";
        document.getElementById("mno").innerHTML = "";
    }
    else
    {
        firestore.collection("userInfo").where("userId","==", uid)
        .get()
        .then((querySnapshot) => {
            querySnapshot.forEach((doc) => {
                document.getElementById("name").innerHTML = "<b>Name : </b>"+doc.data().name;
                document.getElementById("add").innerHTML = "<b>Address : </b>"+doc.data().address;
                document.getElementById("mno").innerHTML = "<b>Mobile No. : </b>"+doc.data().no;
            });
        })
        .catch((error) => {
            console.log("Error getting documents: ", error);
        });
    }
}

function deleteAllRecord()
{
    var len =0;
    swal({
        title: "Are You Sure ?",
        text: "Once you delete all record you cant back it again",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) 
        {
            firestore.collection("SerchQuerys").get().then((querySnapshot) => {
                if(querySnapshot.docs.length == 0)
                {
                    swal("Thier is no record for delete")
                }
                else
                {
                    document.getElementById("btndelete").classList.add("d-none")
                    document.getElementById("btndeleting").classList.remove("d-none");
                }
                querySnapshot.forEach((doc) => {
                    len++;
                    firestore.collection("SerchQuerys").doc(doc.id).delete().then(() => {
                        console.log("Document successfully deleted!");
                    }).catch((error) => {
                        console.error("Error removing document: ", error);
                    });
                    if(len == querySnapshot.docs.length)
                    {
                        table.classList.add("d-none");
                        loadBtn.classList.add("d-none");
                        loadingBtn.classList.add("d-none");
                        document.getElementById("btndelete").classList.remove("d-none")
                        document.getElementById("btndeleting").classList.add("d-none");
                        swal("All rcord deleted successfully");
                    }
                });
            });

        }
    });    
}