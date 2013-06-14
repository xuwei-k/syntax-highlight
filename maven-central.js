javascript:(
(function(org){
  if((org==null)||(org==""))return;
  (function(name){
    if((name==null)||(name==""))return;
    (function(version){
      if((version==null)||(version==""))return;
      var form = document.createElement("form");
      var add  = function(name,value){
        var e = document.createElement("input");
        e.setAttribute("name",name);
        e.setAttribute("value",value);
        e.setAttribute("type","hidden");
        form.appendChild( e );
        return;
      };
      add("download", "on" );
      add("url","http://repo1.maven.org/maven2/" + org + "/" + name + "/" + version + "/" + name + "-" + version + "-sources.jar");
      form.setAttribute("action" ,"http://syntax-highlight.appspot.com/source.zip" );
      form.setAttribute("method" ,"post" );
      document.body.appendChild( form );
      form.submit();
    })(prompt("please input version"))
  })(prompt("please input name"))
})(prompt("please input org name"))
)

