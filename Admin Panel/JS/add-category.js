var pinDiv = document.getElementById('pinDiv');
var mainDiv = document.getElementById('mainDiv');
var subCatDiv = document.getElementById('subCatDiv');
var shopDiv = document.getElementById('shopDiv');
var shop = document.getElementById('shop');
var selectType = document.getElementById('type');
var subInpute = document.getElementById('sub');
var mainCatInput = document.getElementById('mainCat');
var pin = document.getElementById('pin');
var smainCat = document.getElementById('smainCat');
var firestore = firebase.firestore();
var fetchData = [];
getStatus();
function getSelectValue(val)
{
    if(val=="Main Category")
    {
        mainDiv.classList.remove('d-none');
        //subDiv.classList.add('d-none');
        subCatDiv.classList.add('d-none');
        pinDiv.classList.add('d-none');
        shopDiv.classList.add('d-none');
    }
    else if(val=="Sub Category")
    {
        subCatDiv.classList.remove('d-none');
        //subDiv.classList.add('d-none');
        mainDiv.classList.add('d-none');
        pinDiv.classList.add('d-none');
        shopDiv.classList.add('d-none');
        getMainCat();
        //addMainCat();
    
    }
    else if(val=="Shop_Status")
    {
        shopDiv.classList.remove('d-none');
        subCatDiv.classList.add('d-none');
        mainDiv.classList.add('d-none');
        pinDiv.classList.add('d-none');
    }
    else
    {
        //subDiv.classList.add('d-none');
        mainDiv.classList.add('d-none');
        subCatDiv.classList.add('d-none');
        pinDiv.classList.remove('d-none');
        shopDiv.classList.add('d-none');
    }
}
var rc = 2;
function createSubMenu()
{   
    document.getElementById('b'+(rc-1)).disabled = true;
    
    var row = document.createElement("DIV");
    row.classList.add("form-row");
    row.setAttribute('id','row'+rc);
    subCatDiv.appendChild(row);
    
    var col1 = document.createElement("DIV");
    col1.classList.add("form-group","col-md-10","col-sm-10","col-8");
    row.appendChild(col1);
    var input = document.createElement("INPUT");
    input.setAttribute('id','subcat'+rc);
    input.classList.add("form-control");
    input.setAttribute("placeholder","Enter Sub Category "+rc);
    col1.appendChild(input);
    
    var col2 = document.createElement("DIV");
    col2.classList.add("form-group","col-md-1","col-sm-1","col-2");
    row.appendChild(col2);
    var col2btn = document.createElement("BUTTON");
    col2btn.classList.add("btn","btn-primary","myBtn","iconBtn");
    col2btn.innerHTML = '<i class="fa fa-plus"></i>';
    col2btn.setAttribute('onclick','createSubMenu()');
    col2.appendChild(col2btn);
    
    var col3 = document.createElement("DIV");
    col3.classList.add("form-group","col-md-1","col-sm-1","col-2");
    row.appendChild(col3);
    var col3btn = document.createElement("BUTTON");
    col3btn.setAttribute('id','b'+rc);
    col3btn.setAttribute('onclick','deleteSubCat(this.id)');
    col3btn.classList.add("btn","btn-danger","iconBtn");
    col3btn.innerHTML = '<i class="fa fa-trash"></i>';
    col3.appendChild(col3btn);
    rc++;
}
function deleteSubCat(id)
{   
    var temp =id.slice(1);
    var ele = document.getElementById('row'+temp);
    ele.remove();
    rc--;
    if(rc!=2)
    {
        document.getElementById('b'+(rc-1)).disabled = false;
    }
}
function getData()
{
    var select = selectType.value;
    
    if(select=="Main Category")
    {
        if(mainCatInput.value=="")
        {
            swal("Please Add Main Category!");
            //alertMsg("alert-danger","Please Add Main Category!",2000);
        }
        else
        {
            saveData('mainCategory',mainCatInput.value,"Main Category")
        }
    }
    else if(select=="Sub Category")
    {
        if(smainCat.value=="")
        {
            swal("First Select Main Category!");
            //alertMsg("alert-danger","First Select Main Category!",2000);
        }
        else 
        {
            var subArray = [];
            if(fetchData[smainCat.value])
            {
                subArray = fetchData[smainCat.value];
            }
            var flag = false;
            for(var i=1; i<(rc);i++)
            {
                var check = document.getElementById('subcat'+i).value;
                if(check=="")
                {
                    swal("Enter Sub Category "+i);
                    //alertMsg("alert-danger","Enter Sub Category "+i,2000);
                    flag = false;
                }
                else
                {
                    subArray.push(check);
                    flag = true;
                }
            }
            if(flag)
            {
                saveSubData(smainCat.value,subArray);
            }
        }
    }
    else if(select=="Pin Code")
    {
        if(pin.value=="")
        {
            swal("Please Add Pin Code!");
            //alertMsg("alert-danger","Please Add Main Category!",2000);
        }
        else if(isNaN(pin.value))
        {
            swal("Pin Code Must be in Digit Only!");
            //alertMsg("alert-danger","Please Add Main Category!",2000);
        }
        else if((pin.value).length<6 || (pin.value).length<7)
        {
            swal("Pin Code Must be 6 Digit Only!");
            //alertMsg("alert-danger","Please Add Main Category!",2000);
        }
        else
        {
            saveData('pinCode',pin.value,"Pin Code")
        }
    }
    else if(select=="Shop_Status")
    {
        if(shop.value=='true')
        {
            saveStatus(true);
        }
        else
        {
            saveStatus(false);
        }
    }
    else
    {
        swal("Select What you want to Add");
        //alertMsg("alert-danger","Select What you want to Add",2000);
    }
}
function saveData(field,val,text)
{
    firestore.collection('Content').doc('Categories').update({
        [field]: firebase.firestore.FieldValue.arrayUnion(val)
    }).then(function(){
        swal("Successful!", text+" Saved Successfully", "success")
        .then(function (){
            document.location.reload(true);
        });
        //alertMsg("alert-success","Record Added Successfully",2000);
        
    });
}
function saveSubData(field,val)
{
    console.log(field+" : "+val);
    firestore.collection('Content').doc('Categories').update({
        [field]: val
    }).then(function(){
        swal("Successful!", "Sub Category Saved Successfully", "success")
        .then(function (){
            document.location.reload(true);
        });
        //alertMsg("alert-success","Record Added Successfully",2000);
        //document.location.reload(true);
    });
}
function getStatus()
{
    firestore.collection('AppInfo').doc('document').get().then(function(doc) {
        shop.value = doc.data().underMaintenance;
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });
}
function saveStatus(val)
{console.log(val);
    firestore.collection('AppInfo').doc('document').update({
        underMaintenance : val,
    }).then(function(){
        swal("Successful!", "Shop Status Upated Successfully", "success")
        .then(function (){
            console.log("shop status updatede");
        });
    });
}

function getMainCat()
{
    firestore.collection('Content').doc('Categories').get().then(function(doc) {
        if (doc.exists) {
            fetchData = doc.data();
            if(doc.data().mainCategory)
            {
                addMainCat(doc.data().mainCategory)
            }
            else
            {
                swal("No Main Category is Added");
                //alertMsg("alert-danger","No Main Category is Added",2000);
            }
            
        } else {
            // doc.data() will be undefined in this case
            console.log("No such document!");
        }
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });
}
function addMainCat(mcat)
{
    while (smainCat.options.length!=1) 
    {
        smainCat.remove(1);
    }
    
    for (var i = 0; i <mcat.length; i++) 
    {
          var item = new Option(mcat[i]);
          smainCat.options.add(item);
    }
}
function setSubCat(name,element)
{
     while (element.options.length!=1) 
    {
          element.remove(1);
    }
    if (fetchData[name]) 
    {
        //subcat.options[0].selected = true;

        for (var i = 0; i <fetchData[name].length; i++) 
        {
              var item = new Option(fetchData[name][i]);
              element.options.add(item);
        }
    }
}
function resetAll()
{
    
}