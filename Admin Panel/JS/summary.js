var firestore = firebase.firestore();
var datei = document.getElementById("datei");
var table = document.getElementById('recordTable');
var tbody = document.getElementById('tableBody');
var loadBtn = document.getElementById('load');
var loadingBtn = document.getElementById('loading');                                                    
var htitle = document.getElementById('htitle'); 
var totaltext = document.getElementById("total");                                                  
var from,to,first,gtotal = 0;
convertDate("first");

function convertDate(check)
{
    if(check == "first")
    {
        table.classList.add('d-none');
        document.getElementById('spinner').classList.remove('d-none');
        removeChild();gtotal = 0;
        var tempDate = new Date();
        //htitle.innerHTML = "<b>Orders Summary("+tempDate.getDate()+"/"+parseInt(tempDate.getMonth())+1+"/"+tempDate.getFullYear()+")</b>"
        from = new Date(tempDate.getFullYear(),tempDate.getMonth(),tempDate.getDate(),00,00,00);
        to = new Date(tempDate.getFullYear(),tempDate.getMonth(),tempDate.getDate(),23,59,59);
        first = firestore.collection("Orders")
            .where('orderDate','>=', from)
            .where('orderDate','<=', to)
            .where('status','==', "delivered")
            .limit(10);
        getSearchResult();
    }
    else
    {
        if(datei.value == "")
        {
            swal("Please Select Date First")
        }
        else
        {
            //htitle.innerHTML = "<b>Orders Summary("+datei.value+")</b>"
            totaltext.classList.add("d-none");
            table.classList.add('d-none');
            document.getElementById('spinner').classList.remove('d-none');
            removeChild();gtotal = 0;
            var darr = (datei.value).split("-").map(function(item){
                return parseInt(item);
            });
            from = new Date(darr[0],darr[1]-1,darr[2],00,00,00);
            to = new Date(darr[0],darr[1]-1,darr[2],23,59,59);
            first = firestore.collection("Orders")
                .where('orderDate','>=', from)
                .where('orderDate','<=', to)
                .where('status','==', "delivered")
                .limit(10);
            getSearchResult();
        }
    }
}

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
            //dataContainer = [];
            totaltext.classList.remove("d-none");
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            first = firestore.collection("Orders")
            .where('orderDate','>=', from)
            .where('orderDate','<=', to)
            .where('status','==', "delivered")
            .startAfter(lastVisible)
            .limit(10);
            documentSnapshots.forEach(function(doc) {
                
                //dataContainer[doc.id]=doc.data();
                gtotal += doc.data().orderTotal;
                totaltext.innerHTML = '<b>Gross Total : </b><i class="fa fa-rupee"></i>  '+gtotal;
                creatTable([setDateObj((doc.data().orderDate).toDate()),doc.data().orderId,doc.data().userNo,doc.data().productDetails.length,doc.data().orderTotal]);
                //doc.data().description,doc.data().category,doc.data().subCategory(doc.data().orderDate).toDate(),doc.data().status
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
    
    for(var i=0;i<5;i++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
        cell.innerText = fieldList[i];
    }
}
function removeChild()
{
    var tbody = document.getElementById('tableBody');
    while(tbody.firstChild)
    {
        tbody.removeChild(tbody.firstChild);
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
function toTimestamp(strDate){
    var datum = Date.parse(strDate);
    return datum/1000;
   }
function printSummary()
{
    var printDiv = document.getElementById('printDiv');
    printJS({
    printable: 'printDiv',
    type: 'html',
    css: 'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'
    //targetStyles: ['*']
 })
}