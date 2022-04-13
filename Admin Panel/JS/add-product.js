var firestore = firebase.firestore();
var title = document.getElementById('title');
var keyword = document.getElementById('keyword');
var spkeyword = document.getElementById('spkeyword');
var semproduct = document.getElementById('semproduct');
var dis = document.getElementById('discreption');
var fileInput = document.getElementById('imgGroup');
var textInput = document.getElementById('textInput');
var cat = document.getElementById('cat');
var subcat = document.getElementById('subcat');
var dorder = document.getElementById('dorder');
var unit = document.getElementById('unit');
var price = document.getElementById('price');
var quntity = document.getElementById('quntity');
var sellDiv = document.getElementById('sellDiv');
var sellUnit = document.getElementById('sellUnit');
var subPrice = document.getElementById('subPrice');
var subMrp = document.getElementById('subMrp');
var sellDigit = document.getElementById('sellDigit');
var visible = document.getElementById('visible');
var hvisible = document.getElementById('hvisible');
var selected = "text";
var ms = false;
var keyword = document.getElementById('keyword');
var spkeyword = document.getElementById('spkeyword');
var semproduct = document.getElementById('semproduct');
var tags = [],sptags = [], tagsKey = [];

getMainCat();

function setUnit(val)
{
    var ele = document.getElementsByClassName('unit');
    for (var i=0; i<ele.length;i++)
    {
        ele[i].innerHTML = "/"+val;
    }
    if(val=="kg"||val=="liter")
    {
        ms = true;
        sellDiv.classList.remove('d-none');
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
        ms = false;
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

var subUnitContainer = [];
var table = document.getElementById('recordTable');
function saveSubSellUnit()
{
    if(sellDigit.value=="")
    {
        swal("Please Enter Pack Digit");
    }
    else if(sellUnit.value=="")
    {
        swal("Please Select Unit First");
    }
    else if(subPrice.value=="")
    {
        swal("Please Enter Sell Unit Price");
    }
    else if((subPrice.value).includes(" "))
    {
        swal("Please Enter Sell Unit Price Without Space");
    }
    else if(subMrp.value=="")
    {
        swal("Please Enter Sell Unit MRP");
    }
    else if((subMrp.value).includes(" "))
    {
        swal("Please Enter Sell Unit MRP Without Space");
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
        //table.classList.remove('d-none');
        var obj = {'pack':sellDigit.value+" "+sellUnit.value,'price':subPrice.value,'mrp':subMrp.value};
        subUnitContainer.push(obj);
        console.log(subUnitContainer);
        creatTable([sellDigit.value+" "+sellUnit.value,subPrice.value,subMrp.value]);
        resetSubSellUnit();
    }
}
var tbody = document.getElementById('tableBody');
var btnid = 1;

function resetSubSellUnit(type)
{
    sellDigit.value="";
    subPrice.value = "";
    subMrp.value = "";
    if(type=="whole")
    {
        while(tbody.firstChild)
        {
            tbody.removeChild(tbody.firstChild);
        }
    }
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
            cell.classList.add('text-center');
            var b = document.createElement('button');
            b.setAttribute('id',btnid);
            b.classList.add('btn','btn-light');
            b.innerHTML= '<i class="fa fa-trash" style="color:#3e5c9a"></i>';
            b.setAttribute('onclick','deleteTableRow(this)')
            cell.appendChild(b);
            if(btnid!=1)
            {
                document.getElementById(btnid-1).disabled=true;
            }
            btnid++;
        }
        else
        {
            cell.innerText=fieldList[i];
        }
    }
}

function deleteTableRow(ele)
{
    var i = ele.parentNode.parentNode.rowIndex;
    table.deleteRow(i);
    btnid--;
    if(btnid>=2)
    {
        document.getElementById(btnid-1).disabled=false;
    }
    subUnitContainer.pop();
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
function verifyData()
{ 
    if(title.value=="")
    {
        swal("Please Enter Title");
    }
    else if(tags.length == 0)
    {
        swal("Please Enter Search Keyword");
    }
    else if(dis.value=="")
    {
        swal("Please Enter Description");
    }
    else if(cat.value=="")
    {
        swal("Please Enter Category");
    }
    else if(unit.value=="")
    {
        swal("Please Select Unit");
    }
    else if(quntity.value=="")
    {
        swal("Please Enter Quntity");
    }
    else if(ms && sellUnit.value=="")
    {
        console.log("Select sellUnit value");
    }
    else if(subUnitContainer.length==0)
    {
        swal("Please Add Pack Details");
    }
    else
    {
       if(selected == "file")
       {
           console.log('img:'+imgurl[0]);
            if(imgurl[0]==undefined)
            {
                swal("Please Upload Images");    
            }
            else
            {
                //getKeyword(keyword.value);
                saveData(imgurl[0]);
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
                //getKeyword(keyword.value);
                saveData(document.getElementById('url').value);
            }
       }
        
    }
}

function saveData(timg)
{
    var tempQty,diff,per,tper;
    diff = parseInt(subUnitContainer[0].mrp) - parseInt(subUnitContainer[0].price);
    tper = (diff/parseInt(subUnitContainer[0].mrp))*100;
    per = Math.round(tper);
    if(unit.value=="kg"||unit.value=="liter")
    {
        tempQty = 1000 * parseInt(quntity.value);
    }
    else if(unit.value=="dozen")
    {
        tempQty = 12 * parseInt(quntity.value);
    }
    else
    {
        tempQty = parseInt(quntity.value); 
    }
    firestore.collection('Product').add({
        date: new Date(),
        title: title.value,
        image: timg,
        description: dis.value,
        category:cat.value,
        subCategory:subcat.value,
        unit:unit.value,
        quntity:tempQty,
        tags:tags,
        suggestTags:sptags,
        similarTags:semproduct.value,
        keyword:tagsKey,
        sellUnit:subUnitContainer,
        packPrice:parseInt(subUnitContainer[0].price),
        packMrp: subUnitContainer[0].mrp,
        discountPer:per,
        visibility:visible.value,
        homeVisible:hvisible.value,
        dailyDeliver:toBool(dorder.value)
    }).then(function(){
        swal("Successful!", "Product Saved Successfully", "success")
        .then(function (){
            resetAllPage();
        });
    });
}
function toBool(string)
{
	if(string === 'true')
    {
      return true;
    } 
    else 
    {
      return false;
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
      cat.remove(1);
    }
    
    for (var i = 0; i <mcat.length; i++) 
    {
          var item = new Option(mcat[i]);
          cat.options.add(item);
    }
}
// function setSubCat(name,element)
// {
//      while (element.options.length!=1) 
//     {
//           element.remove(1);
//     }
//     if (fetchData[name]) 
//     {
//         for (var i = 0; i <fetchData[name].length; i++) 
//         {
//               var item = new Option(fetchData[name][i]);
//               element.options.add(item);
//         }
//     }
// }

function resetAllPage()
{
    arrayList =[];
    imgurl = [];
    tags =[];
    sptags =[];
    tagsKey = [];
    title.value = "";
    keyword.value = "";
    spkeyword.value = "";
    semproduct.value = "";
    dis.value = "";
    cat.value = "";
    dorder.selectedIndex = 0;
    unit.value = "";
    quntity.value = "";
    document.getElementById('checkBox').checked = false;
    document.getElementById('files').value = "";
    document.getElementById('send').disabled = false;
    document.getElementById('url').value = "";
    sellUnit.value = "";
    resetSubSellUnit("whole");
    subUnitContainer = [];
    btnid = 1;
    selectInput(false);
    ms = false;
    removeAllTags();
}

/*for tag system*/
function addTags(val)
{
    var par = document.getElementById('tagContainer');
    var tag = document.createElement('span');
    tag.setAttribute('id',val);
    tag.classList.add('badge','badge-pill','badge-primary');
    tag.innerHTML = val+' <span class="closetag" onclick="removeTags(this)">&times;</span>';
    par.appendChild(tag);
    tags.push(val);
    createKeywords(val);
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
            //let str = (keyword.value).replaceAll(" ","");
            addTags((keyword.value).toLowerCase());
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
function addspTags(val)
{
    var par = document.getElementById('sptagContainer');
    var tag = document.createElement('span');
    tag.setAttribute('id',"s"+val);
    tag.classList.add('badge','badge-pill','badge-primary');
    tag.innerHTML = val+' <span class="closetag" onclick="removespTags(this)">&times;</span>';
    par.appendChild(tag);
    sptags.push(val);
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
            addspTags((spkeyword.value).toLowerCase());
            spkeyword.value = "";
        }
    }
});

function dbTagSave()
{
    if(cat.value == "")
    {
        swal("Please Select Category First");
    }
    else if(semproduct.value == "")
    {
        swal("Same Product Tag Not Be Blank");
    }
    else
    {
        firestore.collection("TagList").doc(cat.value).set({
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