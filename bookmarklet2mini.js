javascript:(function(url,branch){if(branch==null)return;var form=document.createElement("form");var add=function(name,value){var e=document.createElement("input");e.setAttribute("name",name);e.setAttribute("value",value);e.setAttribute("type","hidden");form.appendChild(e);return;};add("mail_address","yourmailaddress");add("mail","on");add("download","on");add("url",url+"/zipball/"+branch);form.setAttribute("action","http://syntax-highlight.appspot.com/source.zip");form.setAttribute("method","post");document.body.appendChild(form);form.submit();})(location.href,prompt("pleaseinputbranchname","master"))
