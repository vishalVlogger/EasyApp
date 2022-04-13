var rc = 1;
var firestore = firebase.firestore();
var dataContainer = [];
var userContainer = [];
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

getDeliveryBoy();

var first = firestore.collection("DailyOrders")
        .where('status','==','ordered')
        .where('date','==',getTodayDate())
        //.orderBy('date','desc')
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
            first = firestore.collection("DailyOrders")
            .where('status','==','ordered')
            .where('date','==',getTodayDate())
            //.orderBy('date','desc')
            .startAfter(lastVisible)
            .limit(10);
            documentSnapshots.forEach(function(doc) {
                dataContainer[doc.id]=doc.data();
                getUserInfo(doc.data().userID);
             creatTable([doc.id,doc.data().date,doc.data().mobileNumber,doc.data().productsList.length,doc.data().status]);
                //doc.data().description,doc.data().category,doc.data().subCategory
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });

}
function getTodayDate()
{   
    var date,day,month,year;
    var tdate = new Date();
    year = tdate.getFullYear();
    month = ("0"+(tdate.getMonth()+1)).slice(-2);
    day = ("0"+tdate.getDate()).slice(-2);

    date = day+"/"+month+"/"+year;
    return date;
}
function getUserInfo(userid)
{
    firestore.collection('userInfo')
    .where('userId','==',userid)
    .get().then(function (documentSnapshots) {
        documentSnapshots.forEach(function(doc) {
            userContainer[doc.data().userId] = doc.data();
        });
    });
}
function creatTable(fieldList)
{   
    //console.log(fieldList);
    var row = document.createElement('TR');
    tbody.appendChild(row);
    
    for(var i=0;i<5;i++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
    }
    //show data
    for(var j=0;j<5;j++)
    {
        if(j==4)
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

//for search filter
function searchBy()
{
    var subKey = document.getElementById('searchKey').value;
    if(subKey=="")
    {
        swal("Enter Mobile No!");
    }
    else
    {
        firestore.collection("DailyOrders")
        .where('mobileNumber','==','+91'+subKey)
        .get().then(function (documentSnapshots) {
            if(documentSnapshots.docs.length==0)
            {
                swal("There is No Record Found!");
            }
            else
            {
                removeChild();
                documentSnapshots.forEach(function(doc) {
                    dataContainer[doc.id]=doc.data();
                    getUserInfo(doc.data().userID);
                    creatTable([doc.id,doc.data().date,doc.data().mobileNumber,doc.data().productsList.length,doc.data().status]);
                });
                table.classList.remove('d-none');
            }
        });
    }
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
    var id = btnID.slice(-28);
    var table = document.getElementById('orderTable');
    
    var tableBody = document.getElementById('orderTableBody');
    document.getElementById('add').innerHTML = '<b>'+userContainer[id].name+'</b><br>'+userContainer[id].address+'<br>'+userContainer[id].pin+'<br>'+userContainer[id].no;
    
    //document.getElementById('loid').innerHTML = '<b>Order Id : </b>'+dataContainer[id].orderId;
    
    addTable.rows[1].cells[1].innerHTML = '<b>'+userContainer[id].name+'</b><br>'+userContainer[id].address+' '+userContainer[id].pin+'<br>'+userContainer[id].no;
    
    //var temp = table.rows[index].cells[0].innerText;
    document.getElementById('ldate').innerHTML = '<b>Order Date : </b>'+dataContainer[id].date;
    console.log(index);
    
    document.getElementById('oid').value = id;
    var field = ['productTitle','productPack','productQty','productPrice'];
    while(tableBody.firstChild)
    {
        tableBody.removeChild(tableBody.firstChild);
    }
    var grandTotal=0;
    for(var i=0;i<(dataContainer[id].productsList.length);i++)
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
                var toty = parseInt(dataContainer[id].productsList[i]['productQty']);
                var tprice = parseInt(dataContainer[id].productsList[i]['productPrice']);
                cell.innerText = toty*tprice;
                grandTotal = grandTotal+(toty*tprice);
               // console.log();
            }
            else
            {
                cell.innerText = dataContainer[id].productsList[i][field[j]];
            }
        }
    }
    document.getElementById('total').innerHTML = '<b>Grand Total : </b>Rs.'+grandTotal;
    
    if(dataContainer[id].status=="delivery")
    {
       mdboyDiv.classList.remove('d-none');
       dboy.value = dataContainer[id].deliveryBoyName;
       printBtn.classList.remove('d-none');
       deliverBtn.classList.remove('d-none');
    }
    else if(dataContainer[id].status=="ordered")
    {
       mdboyDiv.classList.remove('d-none');
       //dboy.selectedIndex = 0;
       dboy.value = "";
       printBtn.classList.remove('d-none');
       deliverBtn.classList.remove('d-none');
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
        firestore.collection('DailyOrders').doc(oid).update({
                status:'delivery',
                assignDate: new Date(),
                deliveryBoyName:select.value,
                deliveryBoyNo: dBoyList[(select.selectedIndex)-1].no,
                deliveryBoyDocId: dBoyList[(select.selectedIndex)-1].docId
            }).then(function(){
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
            firestore.collection('DailyOrders').doc(oid).update({
                status:'cancelled',
                orderCancelBy:'admin',
                cancellationReason: value
            }).then(function(){
                table.deleteRow(index);
                swal("Successful!", "Order Cancelled", "success");
            });
      }
    });
    
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
    