javascript:(function(url)%7B(function(branch)%7Bvar%20form=document.createElement(%22form%22);var%20add=function(name,value)%7Bvar%20e=document.createElement(%22input%22);e.setAttribute(%22name%22,name);e.setAttribute(%22value%22,value);e.setAttribute(%22type%22,%22hidden%22);form.appendChild(e);return;};add(%22download%22,%22on%22);add(%22url%22,url+%22/zipball/%22+branch);form.setAttribute(%22action%22,%22http://syntax-highlight.appspot.com/source.zip%22);form.setAttribute(%22method%22,%22post%22);document.body.appendChild(form);form.submit();})(prompt(%22please%20input%20branch%20name%22,%22master%22));})(%22http://github.com/%22+prompt(%22please%20input%20project%20name%22));
