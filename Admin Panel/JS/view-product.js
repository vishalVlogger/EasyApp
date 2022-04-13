var firestore = firebase.firestore();
var loadBtn = document.getElementById('load');
var loadingBtn = document.getElementById('loading');
var cat = document.getElementById('cat');
var utitle = document.getElementById('utitle');
//var ukeyword = document.getElementById('ukeyword');
var udis = document.getElementById('udis');
var ucat = document.getElementById('ucat');
var uunit = document.getElementById('uunit');
var uquntity = document.getElementById('uquntity');
var packTable = document.getElementById('recordTable');
var tableBody = document.getElementById('tableBody');
var recordTable = document.getElementById('recordTable');
var unitTag = document.getElementById('unitTag');
var visible = document.getElementById('visible');
var hvisible = document.getElementById('hvisible');
var dataContainer = [];
var imgBox = document.getElementById('imgBox');
var fileInput = document.getElementById('imgGroup');
var textInput = document.getElementById('textInput');
var printList = document.getElementById('printList');
var listTableBody = document.getElementById('listTableBody');
var selected = "text";
var tags = [],sptags = [], tagsKey = [];
var first, firstFilterType = "", esuindex, esuType;
getMainCat();

function setUnit(val)
{
    unitTag.innerHTML = "/"+val;
    
    if(val=="kg"||val=="liter")
    {
        if(val=="kg")
        {
            addSubUnit('kg','gm');
        }
        else
        {
            addSubUnit('liter','ml');
        }
    }
    else if(val=="dozen")
    {
        addSubUnit('dozen','half-dozen');
    }
    else
    {
        while (sellUnit.options.length) 
        {
          sellUnit.remove(0);
        }
    
        var oitem = new Option(val);
        sellUnit.options.add(oitem);
    }
}
function addSubUnit(m,s)
{
    while (sellUnit.options.length) 
    {
      sellUnit.remove(0);
    }
    var item1 = new Option(m);
    sellUnit.options.add(item1);
    
    var item2 = new Option(s);
    sellUnit.options.add(item2);
}
//set selected sub unit
function setSelectedValue(temp)
{
    var j=0;
    for(var i = 0; i < sellUnit.length; i++)
    {
        if(sellUnit.options[i].value==temp[j].pack)
        {
           sellUnit.options[i].selected = true;
           j++;
        }
    }
}
//get product
getAllProduct();
function getAllProduct(type)
{
    if(type == "filter")
    {
        removeChild();
        first = firestore.collection("Product")
        .where("quntity","<=",0)
        //.orderBy('date','desc')
        .limit(8);
        //firstFilterType = "filter";
        getSearchResult();
    }
    else
    {
        removeChild();
        //firstFilterType = "";
        first = firestore.collection("Product")
        .orderBy('date','desc')
        .limit(8);
        getSearchResult();
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
            //alertMsg("alert-danger","There is No Record",2000);
            swal("There is No Record");
            document.getElementById('spinner').classList.add('d-none');
            loadBtn.classList.add('d-none');
            loadingBtn.classList.add('d-none');
        }
        else
        {
            document.getElementById('spinner').classList.add('d-none');
            if(firstFilterType == "filter")
            {
                first = firestore.collection("Product")
                .where("quntity","<=",0)
                //.orderBy('date','desc')
                .startAfter(lastVisible)
                .limit(8);
            }
            else
            {
                first = firestore.collection("Product")
                .orderBy('date','desc')
                .startAfter(lastVisible)
                .limit(8);
            }
            documentSnapshots.forEach(function(doc) {
               dataContainer[doc.id]= doc.data(); createCard(doc.id,doc.data().image,doc.data().title,doc.data().category,doc.data().quntity,doc.data().sellUnit[0].pack);
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });
}

function removeChild()
{
    var mdiv = document.getElementById('viewDiv');
    while(mdiv.firstChild)
    {
        mdiv.removeChild(mdiv.firstChild);
    }
}
function createCard(id,image,title,fcat,qty,pack)
{   
    var out = false;
    if(qty<=0)
    {
        out = true;
    }
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

    var h = document.createElement("P");
    h.classList.add('blogShow','card-title');
    h.setAttribute('id','h'+id);
    h.innerHTML='<b>'+title+'</b>';
    cardBody.appendChild(h);

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
    btnDelete.setAttribute('onclick','blogDelete(this.id)');
    cardFooter.appendChild(btnDelete);

    var btnEdit = document.createElement("BUTTON");
    btnEdit.classList.add("btn","btn-info","float-right","mr-2");
    btnEdit.innerHTML="<i class='fa fa-pencil'></i>";
    btnEdit.setAttribute('id','u'+id);
    btnEdit.setAttribute('onclick','blogEdit(this.id)');
    btnEdit.setAttribute('data-toggle','modal');
    btnEdit.setAttribute('data-target','#exampleModal');
    cardFooter.appendChild(btnEdit);
    
    if(out)
    {
        var lout = document.createElement('span');
        lout.innerHTML = "Out of Stock";
        lout.classList.add("float-left",'badge','badge-danger');
        cardFooter.appendChild(lout);
    }
}
function blogDelete(docID)
{
    var tempCard = document.getElementById('temp'+docID);
    swal({
        title: "Are You Sure ?",
        text: "Once Product Deleted You can't get it back!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) 
        {
            firestore.collection("Product").doc(docID).delete().then(function() {
                swal("Successful!", "Product Successfully Deleted", "success");
                //alertMsg("alert-success","Product Successfully Deleted",2000);
                getPathStorageFromUrl(document.getElementById('imgs'+docID).src);
                tempCard.remove();
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

function checkImgUpdate()
{
    var check = document.getElementById('ynCheck').checked;
    if(check)
    {
       if(selected == "file")
       {
            if(imgurl[0]==undefined)
            {
                swal("Please Upload Images");    
            }
            else
            {
                verifyUpdateData(imgurl[0]);
            }
       }
       else
       {
            if(document.getElementById('url').value=="")
            {
                swal("please Enter Image Link"); 
            }
            else
            {
                verifyUpdateData(document.getElementById('url').value);
            }
       }
    }
    else
    {
        let id = document.getElementById('updateId').value;
        verifyUpdateData(dataContainer[id].image);
    }
}

function viewImgDiv(sobj)
{
    if(sobj)
    {
        imgBox.classList.remove('d-none');
    }
    else
    {
        imgBox.classList.add('d-none');
    }
}
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

var packContainer;
function blogEdit(btnId)
{
    tags = []; sptags = []; tagsKey = [];
    removeAllTags();
    visible.value = "show" ;
    hvisible.value = "hide" ;
    sellDigit.value = ""; subPrice.value = ""; subMrp.value = ""; semproduct.value = "";spkeyword.value = "";
    var id = btnId.slice(1);
    document.getElementById('updateId').value = id;
    packContainer = dataContainer[id].sellUnit; //console.log(packContainer);
    if(dataContainer[id].tags != undefined)
    {
        tagsKey = dataContainer[id].keyword;
        tags = dataContainer[id].tags;
        for(var i=0;i<tags.length;i++)
        {
            addTags(tags[i]);
        }
        sptags = dataContainer[id].suggestTags;   
        for(var j=0;j<sptags.length;j++)
        {
            addspTags(sptags[j]);
        }
        visible.value = dataContainer[id].visibility;
        hvisible.value = dataContainer[id].homeVisible;
    }

    utitle.value = dataContainer[id].title;
    udis.value = dataContainer[id].description;
    ucat.value = dataContainer[id].category;
    getDataList(dataContainer[id].category);
    uunit.value = dataContainer[id].unit;
    setUnit(dataContainer[id].unit);
    unitTag.innerHTML = "/"+dataContainer[id].unit;
    //below logic is hide for temp
    if(dataContainer[id].unit=='kg'||dataContainer[id].unit=='liter')
    {
        uquntity.value = parseInt(dataContainer[id].quntity)/1000;
    }
    else if(dataContainer[id].unit=='dozen')
    {
        uquntity.value = parseInt(dataContainer[id].quntity)/12;
    }
    else
    {
        uquntity.value = dataContainer[id].quntity;
    }
    //set pack details table tableBody
    while(tableBody.firstChild)
    {
        tableBody.removeChild(tableBody.firstChild);
    }
    var tempArr = ["pack","price","mrp"]; 
    for(var i=0;i<dataContainer[id].sellUnit.length;i++)
    {
        var row = document.createElement('TR');
        tableBody.appendChild(row);
        for(var j=0;j<5;j++)
        {
            var cell = document.createElement("TD");
            row.appendChild(cell);
            if(j==3)
            {
                var btn = document.createElement('button');
                btn.setAttribute('onclick','editSellUnit(this)')
                btn.classList.add('btn','btn-primary');
                btn.innerHTML = '<i class="fa fa-edit"></i>'
                cell.appendChild(btn);
            }
            else if(j==4)
            {
                var btn = document.createElement('button');
                btn.setAttribute('onclick','deleteSellUnit(this)')
                btn.classList.add('btn','btn-danger');
                btn.innerHTML = '<i class="fa fa-trash"></i>'
                cell.appendChild(btn);
            }
            else
            {
                cell.innerText = dataContainer[id].sellUnit[i][tempArr[j]];
            }
        }
    }
}

function deleteSellUnit(ele)
{
    var i = ele.parentNode.parentNode.rowIndex;
    packTable.deleteRow(i);
    packContainer.splice(i-1,1);
}
function editSellUnit(ele)
{
    esuindex = (ele.parentNode.parentNode.rowIndex)-1;
    esuType = "exitOne";
    var r = ele.parentNode.parentNode;
    sellDigit.value = (r.children[0].innerText).replace(/\D/g,'');
    sellUnit.value = (r.children[0].innerText).replace(/[^a-zA-Z]+/g,'');
    subPrice.value = r.children[1].innerText;
    subMrp.value = r.children[2].innerText;
}
function saveSellUnit()
{
    if(sellDigit.value=="")
    {
        swal("Please Enter Pack Digit")
    }
    else if(sellUnit.value=="")
    {
        swal("Please Select Unit First");
    }
    else if(subPrice.value=="")
    {
        swal("Please Enter Sell Unit Price");
    }
    else if(subMrp.value=="")
    {
        swal("Please Enter Sell Unit MRP");
    }
    else if(isNaN(subPrice.value)||isNaN(subMrp.value))
    {
        swal("Please Enter Only Digit in Sell Unit Section");
    }
    else if(parseInt(subPrice.value)>parseInt(subMrp.value))
    {
        swal("Please Enter Valid Price & MRP");
    }
    else
    {
        if(esuType == "exitOne")
        {
            packContainer[esuindex].pack = sellDigit.value+" "+sellUnit.value;
            packContainer[esuindex].price = subPrice.value;
            packContainer[esuindex].mrp = subMrp.value;
            recordTable.rows[esuindex+1].cells[0].innerText = sellDigit.value+" "+sellUnit.value;
            recordTable.rows[esuindex+1].cells[1].innerText = subPrice.value;
            recordTable.rows[esuindex+1].cells[2].innerText = subMrp.value;
            esuType = "";
        }
        else
        {
            esuindex = null;
            esuType = "";
            var obj = {'pack':sellDigit.value+" "+sellUnit.value,'price':subPrice.value,'mrp':subMrp.value};
            packContainer.push(obj);
            // add to table
            var tempArr = ["pack","price","mrp"];
            var row = document.createElement('TR');
            tableBody.appendChild(row);
            for(var j=0;j<5;j++)
            {
                var cell = document.createElement("TD");
                row.appendChild(cell);
                if(j==3)
                {
                    var btn = document.createElement('button');
                    btn.setAttribute('onclick','editSellUnit(this)')
                    btn.classList.add('btn','btn-primary');
                    btn.innerHTML = '<i class="fa fa-edit"></i>'
                    cell.appendChild(btn);
                }
                else if(j==4)
                {
                    var btn = document.createElement('button');
                    btn.setAttribute('onclick','deleteSellUnit(this)')
                    btn.classList.add('btn','btn-danger');
                    btn.innerHTML = '<i class="fa fa-trash"></i>'
                    cell.appendChild(btn);
                }
                else
                {
                    cell.innerText = packContainer[(packContainer.length)-1][tempArr[j]];
                }
            }
        }
        
    }
}
function verifyUpdateData(fimg)
{
    if(utitle.value=="")
    {
        swal("Title Not be Blank");
    }
    else if(udis.value=="")
    {
        swal("Discreption Not be Blank");
    }
    else if(tags.length ==0)
    {
        swal("Please Add Search Keyword Tags");
    }
    else if(uquntity.value=="")
    {
        swal("Quntity Not be Blank");
    }
    else if(packContainer.length==0)
    {
        swal("Add Atleast One Pack");
    }
    else
    {
        updateData(fimg);
    }
}
function updateData(img)
{
    var quntity;
    
    if(uunit.value=="kg"||uunit.value=="liter")
    {
        quntity = parseFloat(uquntity.value)*1000;
    }
    else if(uunit.value=="dozen")
    {
        quntity = parseInt(uquntity.value)*12;
    }
    else
    {
        quntity = parseInt(uquntity.value);
    }
    
    var doc = document.getElementById('updateId').value;
    firestore.collection("Product").doc(doc).update({
        title: utitle.value,
        description: udis.value,
        category:ucat.value,
        unit:uunit.value,
        quntity:quntity,
        sellUnit:packContainer,
        image:img,
        tags:tags,
        suggestTags:sptags,
        similarTags:semproduct.value,
        keyword:tagsKey,
        visibility:visible.value,
        homeVisible:hvisible.value,
    }).then(function() {
        document.getElementById("h"+doc).innerHTML = utitle.value;
        document.getElementById("c"+doc).innerHTML = ucat.value;
        document.getElementById("imgs"+doc).src = img;
        swal("Successful!", "Product successfully Updated!", "success");
        dataContainer[doc].keyword = tagsKey;
        dataContainer[doc].tags = tags;
        dataContainer[doc].suggestTags = sptags;
        dataContainer[doc].title = utitle.value;
        dataContainer[doc].description = udis.value;
        dataContainer[doc].category = ucat.value;
        dataContainer[doc].unit = uunit.value;
        dataContainer[doc].image = img;
        dataContainer[doc].quntity = quntity;
        dataContainer[doc].sellUnit = packContainer;
        dataContainer[doc].visibility = visible.value;
        dataContainer[doc].homeVisible = hvisible.value;

        $('#exampleModal').modal('hide');
    }).catch(function(error) {
        console.error("Error in Updating document: ", error);
    });
}
function signOut()
{
    firebase.auth().signOut();
    window.location.replace("index.html");
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
            val = subKey.toLowerCase();
        }
        else if(value!="")
        {
            val = value;
        }
        loadingBtn.classList.add('d-none');
        loadBtn.classList.add('d-none');
        //document.getElementById('spinner').classList.remove('d-none');
        removeChild();

        if(firstFilterType == "filter")
        {
            filterFirst = firestore.collection("Product")
            .where(fname,op,val)
            .where("quntity","<=",0)
            .limit(8);
        }
        else
        {
            filterFirst = firestore.collection("Product")
            //.where('keyword','array-contains',subKey)
            .where(fname,op,val)
            .limit(8);
        }
        
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
            loadBtn.classList.add('d-none');
            loadingBtn.classList.add('d-none');
        }
        else
        {
            //dataContainer = [];
            //document.getElementById('spinner').classList.add('d-none');
            if(firstFilterType == "filter")
            {
                filterFirst = firestore.collection("Product")
                .where(fname,op,val)
                .where("quntity","<=",0)
                .startAfter(lastVisible)
                .limit(8);
            }
            else
            {
                filterFirst = firestore.collection("Product")
                .where(fname,op,val)
                .startAfter(lastVisible)
                .limit(8);
            }
            documentSnapshots.forEach(function(doc) {
               dataContainer[doc.id]= doc.data();  
               createCard(doc.id,doc.data().image,doc.data().title,doc.data().category,doc.data().quntity,doc.data().sellUnit[0].pack);
            });
            loadBtn.classList.remove('d-none');
            loadingBtn.classList.add('d-none');
        }  
    });
}

function showOutProduct(val)
{console.log("in show outod function")
    if(val)
    {
        printList.classList.remove("d-none");
        firstFilterType = "filter";
        if(cat.value != "")
        {
            getFilterResult('category','==',cat.value);
        }
        else
        {
            getAllProduct("filter");
        }
    }
    else
    {
        printList.classList.add("d-none");
        firstFilterType = "";
        if(cat.value != "")
        {
            getFilterResult('category','==',cat.value);
        }
        else
        {
            getAllProduct();
        }
    }
}


//set both select value
function getMainCat()
{
    firestore.collection('Content').doc('Categories').get().then(function(doc) {
        if (doc.exists) {
            fetchData = doc.data()
            //console.log("fd"+fetchData);
            addMainCat(doc.data().mainCategory)
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
    while (cat.options.length!=1) 
    {
      ucat.remove(1);
      cat.remove(1);
    }
    
    for (var i = 0; i <mcat.length; i++) 
    {
          var item = new Option(mcat[i]);
          var uitem = new Option(mcat[i]);
          ucat.options.add(uitem);
          cat.options.add(item);
    }
}

// tag system coding
/*for tag system*/
function addTags(val,save)
{
    var par = document.getElementById('tagContainer');
    var tag = document.createElement('span');
    tag.setAttribute('id',val);
    tag.classList.add('badge','badge-pill','badge-primary');
    tag.innerHTML = val+' <span class="closetag" onclick="removeTags(this)">&times;</span>';
    par.appendChild(tag);
    if(save)
    {
        tags.push(val);
        createKeywords(val);
    }
    //console.log(tags,tagsKey);
}

function removeTags(ele)
{
    tagsKey = [];
    let tname = ele.parentElement.id;
    let i = tags.indexOf(tname);
    tags.splice(i,1);
    
    for(var k = 0;k<tags.length;k++)
    {
        createKeywords(tags[k]);
    }
    
    ele.parentElement.remove();
    //console.log(tags,tagsKey);
}
function removeAllTags()
{
    var tagbody = document.getElementById('tagContainer');
    while(tagbody.firstChild)
    {
        tagbody.removeChild(tagbody.firstChild);
    }
    //another 
    var sptagbody = document.getElementById('sptagContainer');
    while(sptagbody.firstChild)
    {
        sptagbody.removeChild(sptagbody.firstChild);
    }
}

keyword.addEventListener("keyup",function(event){
    //console.log(event);
    if(event.keyCode === 13)
    {
        if(keyword.value == "" || keyword.value == " ")
        {
            swal("Keyword Not be Blank");
            keyword.value = "";
        }
        else
        {
            //let str = .replaceAll(" ","");
            addTags((keyword.value).toLowerCase(),true);
            keyword.value = "";
        }
    }
})
//tags tagsKey

/*for keyword generation*/
function createKeywords(name) 
{   
    const arrName = [];
    let curName = '';
    name.split('').forEach(letter => {
    curName += letter;
    arrName.push(curName.trim());
    });
    tagsKey = tagsKey.concat(arrName);

}
//suggested product tags
function addspTags(val,save)
{
    var par = document.getElementById('sptagContainer');
    var tag = document.createElement('span');
    tag.setAttribute('id',"s"+val);
    tag.classList.add('badge','badge-pill','badge-primary');
    tag.innerHTML = val+' <span class="closetag" onclick="removespTags(this)">&times;</span>';
    par.appendChild(tag);
    if(save)
    {
        sptags.push(val);
    }
}

function removespTags(ele)
{
    let tname = ele.parentElement.id;
    let i = sptags.indexOf(tname.slice(1));
    sptags.splice(i,1);
    ele.parentElement.remove();;
}
spkeyword.addEventListener("keyup",function(event){
    //console.log(event);
    if(event.keyCode === 13)
    {
        if(spkeyword.value == "" || spkeyword.value == " ")
        {
            swal("Tag Not be Blank");
            spkeyword.value = "";
        }
        else
        {
            //let str = (spkeyword.value).replaceAll(" ","");
            addspTags((spkeyword.value).toLowerCase(),true);
            spkeyword.value = "";
        }
    }
});

function dbTagSave()
{
    if(ucat.value == "")
    {
        swal("Please Select Category First");
    }
    else if(semproduct.value == "")
    {
        swal("Same Product Tag Not Be Blank");
    }
    else
    {
        firestore.collection("TagList").doc(ucat.value).set({
            tags:firebase.firestore.FieldValue.arrayUnion(semproduct.value),
        },{merge:true}).then(function(){
            swal("Tag Saved Successfully.");
        }).catch(function(error){
            console.log("Tag Save Error:"+error);
        });
    }
}
function getDataList(docid)
{
    firestore.collection("TagList").doc(docid).get().then(function(doc){
        if(doc.exists)
        {
            setDataList(true,doc.data().tags);
        }
        else
        {
            setDataList(false,"");
            console.log("no data list is stored");
        }
    }).catch(function(error){
        console.log("datalist get Error:"+error);
    });
}
function setDataList(check,arr)
{
    let dlist = document.getElementById("spsuggest");
    while (dlist.options.length) 
    {
      dlist.children[0].remove();
    }
    if(check)
    {
        for(var i=0;i<arr.length;i++)
        {
            var op = document.createElement("option");
            op.text = arr[i];
            //op.value = arr[i];
            dlist.appendChild(op);
            //var op = new Option(arr[i]);
            //dlist.options.add(op);
        }
    }
 }
printList.addEventListener("click", function(){
    var tbody = document.getElementById("listTableBody");
    var filterQuery,scount = 0;
    //remove elemente
    while(tbody.firstChild)
    {
        tbody.removeChild(tbody.firstChild);
    }
    
    if(cat.value =="")
    {
        document.getElementById("plh").innerHTML = "<b>Out of stock product list(All Category)</b>";
        filterQuery = firestore.collection("Product").where("quntity", "<=", 0);
    }
    else
    {
        document.getElementById("plh").innerHTML = "<b>Out of stock product list("+cat.value+")</b>";
        filterQuery = firestore.collection("Product").where("quntity", "<=", 0).where("category", "==", cat.value);
    }
    filterQuery.get().then((querySnapshot) => {
        if(querySnapshot.docs.length==0)
        {
            swal("Thier is no out of stock product");
        }
        else
        {
            querySnapshot.forEach((doc) => {
                scount++
                addElementToListTable(scount,doc.data().title);
                if(scount == querySnapshot.docs.length)
                {
                    printBill();
                }
            });
        }
    })
    .catch((error) => {
        console.log("Error getting documents: ", error);
    });
    
});
function addElementToListTable(no,pname)
{
    var tbody = document.getElementById("listTableBody");
    var row = document.createElement('TR');
    tbody.appendChild(row);
    for(var j=0;j<2;j++)
    {
        var cell = document.createElement("TD");
        row.appendChild(cell);
        if(j==0)
        {
            cell.innerText = no;
        }
        else
        {
            cell.innerText = pname;
        }
    }
}
function printBill()
{
    var printDiv = document.getElementById('printDiv');
    printDiv.classList.remove('d-none');
    printJS({
        printable: 'printDiv',
        type: 'html',
        css: 'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'
    });
}