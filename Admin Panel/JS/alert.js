function alertMsg(type,msg,t)
{
    var main = document.getElementById("msg");
    
    var div = document.createElement("DIV");
    div.classList.add('alert','alert-dismissible','fade','show',type);
    div.setAttribute("role","alert");
    div.innerHTML = msg;
    main.appendChild(div);
    
    var btn = document.createElement("BUTTON");
    btn.setAttribute('type','button');
    btn.setAttribute('class','close');
    btn.setAttribute('data-dismiss','alert');
    btn.setAttribute('aria-label','Close');
    var span = document.createElement("SPAN");
    span.innerHTML = '&times';
    span.setAttribute('aria-hidden','true');
    btn.appendChild(span);
    div.appendChild(btn);
    
    closeAlert(t);
}
function closeAlert(time)
{
    window.setTimeout(function() {
            $(".alert").fadeTo(500, 0).slideUp(500, function(){
                $(this).remove(); 
            });
        }, time);
}