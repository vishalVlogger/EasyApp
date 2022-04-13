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
var select = document.getElementById('dboy');
var printBtn = document.getElementById('printBtn');
var deliverBtn = document.getElementById('deliverBtn');
var cancelBtn = document.getElementById('cancelBtn');
getDeliveryBoy();

firestore.collection("Orders").where("status", "==", "ordered").orderBy('orderDate','desc')
    .onSnapshot((querySnapshot) => {
        if(querySnapshot.docs.length==0)
        {
            swal("There is no Record Found");
            document.getElementById('spinner').classList.add('d-none');
        }
        else
        {
            removeChild();
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            querySnapshot.forEach((doc) => {
                dataContainer[doc.id]=doc.data();
                setDate(doc.id,(doc.data().orderDate).toDate(),doc.data().orderId,doc.data().userNo,doc.data().productDetails.length,doc.data().orderTotal,doc.data().status);
            });
        }
    });

function setDate(id,tdate,orderId,no,item,total,status)
{   //console.log('in set date function');
    var date,day,month,year,time,hours,minutes;
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
 
    var field = [id,date,orderId,no,item,total,status];
    creatTable(field);
}
        
function creatTable(fieldList)
{   
    //console.log(fieldList);
    var row = document.createElement('TR');
    tbody.appendChild(row);
    
    for(var i=0;i<7;i++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
    }
    //show data
    for(var j=0;j<7;j++)
    {
        if(j==6)
        {
            var btn = document.createElement("button");
            btn.setAttribute('id','vbtn'+fieldList[0]);
            btn.setAttribute('data-toggle','modal');
            btn.setAttribute('data-target','#orderDetails');
            btn.classList.add('btn','btn-primary');
            btn.setAttribute('onclick','showOrder(this.id,this)');
            btn.innerHTML = '<i class="fa fa-eye"></i>';
            table.rows[rc].cells[j].appendChild(btn);
        }
        else
        {
            table.rows[rc].cells[j].innerText=fieldList[j+1];
        }
    }
    rc++;
}
function printDoc()
{
    window.print();
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
        swal("Enter Order ID!");
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
        //console.log(fname+", "+op+", "+val);
        loadingBtn.classList.add('d-none');
        loadBtn.classList.add('d-none');
        //document.getElementById('spinner').classList.remove('d-none');
        removeChild();
        filterFirst = firestore.collection("Orders")
        .where(fname,op,val)
        .orderBy('orderDate','desc')
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
            //dataContainer = [];
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            
            filterFirst = firestore.collection("Orders")
            .where(fname,op,val)
            .orderBy('orderDate','desc')
            .startAfter(lastVisible)
            .limit(8);
            documentSnapshots.forEach(function(doc) {
               // console.log(doc.data());
               dataContainer[doc.id]=doc.data();
                
                setDate(doc.id,(doc.data().orderDate).toDate(),doc.data().orderId,doc.data().userNo,doc.data().productDetails.length,doc.data().orderTotal,doc.data().status);
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
var index;
var addTable = document.getElementById('addTable');
var mdboyDiv = document.getElementById('mdboyDiv');
function showOrder(btnID,ele)
{
    var dboy = document.getElementById('dboy');
    index = ele.parentNode.parentNode.rowIndex;
    var id = btnID.slice(-20);
    //var table = document.getElementById('orderTable');
    document.getElementById('modelTitle').innerText = 'Order Details ('+dataContainer[id].orderId+')';
    var tableBody = document.getElementById('orderTableBody');
    document.getElementById('add').innerHTML = '<b>'+dataContainer[id].userInfo['name']+'</b><br>'+dataContainer[id].userInfo['address']+'<br>'+dataContainer[id].userInfo['pin']+'<br>'+dataContainer[id].userInfo['no'];
    document.getElementById("notes").innerHTML = dataContainer[id].note != undefined ? '<b>Note: </b>'+dataContainer[id].note :"";
    document.getElementById('loid').innerHTML = '<b>Order Id : </b>'+dataContainer[id].orderId;
    
    addTable.rows[1].cells[1].innerHTML = '<b>'+dataContainer[id].userInfo['name']+'</b><br>'+dataContainer[id].userInfo['address']+' '+dataContainer[id].userInfo['pin']+'<br>'+dataContainer[id].userInfo['no'];
    
    var temp = table.rows[index].cells[0].innerText;
    document.getElementById('ldate').innerHTML = '<b>Order Date : </b>'+temp.slice(0,10);
    //console.log(index);
    
    document.getElementById('oid').value = id;
    //document.getElementById('coid').value = id;
    var field = ['productTitle','productPack','productQty','productPrice'];
    while(tableBody.firstChild)
    {
        tableBody.removeChild(tableBody.firstChild);
    }
    
    for(var i=0;i<dataContainer[id].productDetails.length;i++)
    {
        var row = document.createElement('TR');
        tableBody.appendChild(row);
        for(var j=0;j<5;j++)
        {
            var cell = document.createElement("TD");
            
            row.appendChild(cell);
            //var temp = field[j];
            if(j==4)
            {
                var toty = parseInt(dataContainer[id].productDetails[i]['productQty']);
                var tprice = parseInt(dataContainer[id].productDetails[i]['productPrice']);
                cell.innerText = toty*tprice;
               // console.log();
            }
            else
            {
                cell.innerText = dataContainer[id].productDetails[i][field[j]];
            }
        }
    }
    document.getElementById('total').innerHTML = '<b>Grand Total : </b>Rs.'+dataContainer[id].orderTotal;
    
    if(dataContainer[id].status=="delivery")
    {
       mdboyDiv.classList.remove('d-none');
       dboy.value = dataContainer[id].deliveryBoyName;
       printBtn.classList.remove('d-none');
       deliverBtn.classList.remove('d-none');
       cancelBtn.classList.remove('d-none');
       document.getElementById('oinfo').innerHTML ="";
    }
    else if(dataContainer[id].status=="ordered")
    {
       mdboyDiv.classList.remove('d-none');
       //dboy.selectedIndex = 0;
       dboy.value = "";
       printBtn.classList.remove('d-none');
       deliverBtn.classList.remove('d-none');
       cancelBtn.classList.remove('d-none');
       document.getElementById('oinfo').innerHTML ="";
    }
    /*else if(dataContainer[id].status=="delivered" || dataContainer[id].status=="cancelled")
    {
        mdboyDiv.classList.add('d-none'); 
    }*/
    else
    {
         mdboyDiv.classList.add('d-none');  
         printBtn.classList.add('d-none');
         deliverBtn.classList.add('d-none');
         cancelBtn.classList.add('d-none');
         if(dataContainer[id].status=="delivered")
         {
             document.getElementById('oinfo').innerHTML = "<b>Order Delivered By:</b></br>"+dataContainer[id].deliveryBoyName;
         }
         else
         {
             document.getElementById('oinfo').innerHTML = "<b>Order Cancelled By:</b></br>"+dataContainer[id].orderCancelBy+"</br><b>Order Cancelled Reasone:</b></br>"+dataContainer[id].cancellationReason;
         }
    }
}
var dBoyList = [];
function getDeliveryBoy()
{
    firestore.collection('DeliveryBoy').orderBy('name').get()
        .then(function(documentSnapshots) {
        documentSnapshots.forEach(function(doc){
            if (doc.exists) {
                var obj = {'docId':doc.id,'name':doc.data().name,'no':doc.data().mobileNo}
                dBoyList.push(obj);
            } else {
                console.log("No Dlivery boy is added!");
            }
        });
        addDeliveryBoy();
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });
}

function addDeliveryBoy()
{
    while (select.options.length!=1) 
    {
      select.remove(1);
    }
    for (var i = 0; i <dBoyList.length; i++) 
    {
          var item = new Option(dBoyList[i].name);
          select.options.add(item);
    }
}
function assignDelivery()
{
    if(select.value=="")
    {
        swal("Please Select Delivery Boy!");
    }
    else
    {
        var oid = document.getElementById('oid').value;
        //console.log("orderdocid:"+oid);
        firestore.collection('Orders').doc(oid).update({
                status:'delivery',
                assignDate: new Date(),
                deliveryBoyName:select.value,
                deliveryBoyNo: dBoyList[(select.selectedIndex)-1].no,
                deliveryBoyDocId: dBoyList[(select.selectedIndex)-1].docId
            }).then(function(){
                sendNotification();
                if(dataContainer[oid].status=="ordered")
                {
                    select.selectedIndex = 0;
                    table.deleteRow(index);
                }
                else
                {
                    dataContainer[oid].deliveryBoyName = select.value;
                }
                $('#orderDetails').modal('hide');
                swal("Successful!", "Order Assign to Delivery Boy Successfully", "success");
            });
    }    
}

function sendNotification()
{
    console.log(dBoyList[(select.selectedIndex)-1].no);
    const xhr = new XMLHttpRequest();
    xhr.onload = function (){
            console.log(this.responseText);
    };
    xhr.open("GET","https://send-notifications-donner.000webhostapp.com/sendDeliveryNoti.php?no="+dBoyList[(select.selectedIndex)-1].no,true);//8788343984",true);
    xhr.send();
}

function cancelOrder()
{
    fixBootstrapModal();
    swal("Enter Order Cancelation Reasone :", {
      content: "input",
      closeOnClickOutside: false,
      button: "Cancel Order",
    })
    .then((value) => {
      if(value=="")
      {
          swal("Please Enter Cancel Reasone");
      }
      else
      {
            restoreBootstrapModal();
            var oid = document.getElementById('oid').value;
            firestore.collection('Orders').doc(oid).update({
                status:'cancelled',
                orderCancelBy:'store maneger',
                cancellationReason: value
            }).then(function(){
                restoreQty(dataContainer[oid].productDetails);
                table.deleteRow(index);
                $('#orderDetails').modal('hide');
                swal("Successful!", "Order Cancelled", "success");
            });
      }
    });
    
}
function restoreQty(prductArr)
{
    for(var i=0;i<prductArr.length;i++)
    {
        var rQty = prductArr[i].productQty * getReducedValue(prductArr[i].productPack);
        getQuntity(prductArr[i].productDocId,rQty);
    }
}
function getQuntity(id,tqty)
{
    firestore.collection('Product').doc(id).get()
    .then(function(doc){
        updateQuntity(doc.id,doc.data().quntity,tqty);
    }).catch(function(error){
       console.log(error); 
    });
}
function updateQuntity(id,oldQty,newQty)
{
    firestore.collection('Product').doc(id).update({
        quntity: oldQty+newQty,
    })
    .then(function(){
        console.log("qty updated......");
    }).catch(function(error){
       console.log(error); 
    });
}
function getReducedValue(pack)
{   //console.log(pack);
    
    var digit = parseInt(pack.replace(/[^0-9]/g,''));
    
    if(pack.includes("kg")||pack.includes("liter"))
    {
        return digit*1000;
    }
    
    else if(pack.includes("half-dozen"))
    {
        return digit*6;
    }
    else if(pack.includes("dozen"))
    {
        return digit*12;
    }
    else
    {
        return digit;
    }
}

// call this before showing SweetAlert:
function fixBootstrapModal() {
  var modalNode = document.querySelector('.modal[tabindex="-1"]');
  if (!modalNode) return;

  modalNode.removeAttribute('tabindex');
  modalNode.classList.add('js-swal-fixed');
}

// call this before hiding SweetAlert (inside done callback):
function restoreBootstrapModal() {
  var modalNode = document.querySelector('.modal.js-swal-fixed');
  if (!modalNode) return;

  modalNode.setAttribute('tabindex', '-1');
  modalNode.classList.remove('js-swal-fixed');
}
function printBill()
{
    var printDiv = document.getElementById('printDiv');
    printDiv.classList.remove('d-none');
    /*var backup = document.body.innerHTML;
    var printDiv = document.getElementById('printDiv').innerHTML;
    document.body.innerHTML = printDiv;
    window.print();*/
    printJS({
    printable: 'printDiv',
    type: 'html',
    css: 'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'
    //targetStyles: ['*']
 })
}

//old code
/*var first = firestore.collection("Orders")
        .where('status','==','ordered')
        .orderBy('orderDate','desc')
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
            //dataContainer = [];
            document.getElementById('spinner').classList.add('d-none');
            table.classList.remove('d-none');
            first = firestore.collection("Orders")
            .where('status','==','ordered')
            .orderBy('orderDate','desc')
            .startAfter(lastVisible)
            .limit(10);
            documentSnapshots.forEach(function(doc) {
                
                dataContainer[doc.id]=doc.data();
                
                setDate(doc.id,(doc.data().orderDate).toDate(),doc.data().orderId,doc.data().userNo,doc.data().productDetails.length,doc.data().orderTotal,doc.data().status);
                //doc.data().description,doc.data().category,doc.data().subCategory
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });

}
*/