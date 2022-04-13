var arrayList = [];

function createKeywords(name) 
{   
    const arrName = [];
    let curName = '';
    name.split('').forEach(letter => {
    curName += letter;
    arrName.push(curName.trim());
    });
    arrayList = arrayList.concat(arrName);         
}

function genKeywords(name)
{
    createKeywords(name);
    var len= name.lenth;
    var v1=name.indexOf(" ");
    if(v1!=-1)
    {
        var newString=name.slice(v1+1,len);
        genKeywords(newString);   
    }
}

function getKeyword(searchString)
{
    genKeywords(searchString.toLowerCase());

}