/**
 * ブラウザで、あるgithubのプロジェクトのトップページを開いてる状態のときに
 * このブックマークレットをクリックすると、プロンプトがでるので
 * そのプロンプトで入力した名前の branch をダウンロードするやつ
 *
 * 入力した branch が存在しないとエラーになる
 */

javascript:(
  function(url,branch){
    if(branch == null)return;

    var form = document.createElement("form");
    var add = function(name,value){
      var e = document.createElement("input");
      e.setAttribute("name",name);
      e.setAttribute("value",value);
      e.setAttribute("type","hidden");
      form.appendChild( e );
      return;
    };
    add("mail_address", "your mail address" );
    add("mail", "on" );
    add("download", "on" );
    add("url", url + "/zipball/" + branch );
    form.setAttribute("action" ,"http://syntax-highlight.appspot.com/source.zip" );
    form.setAttribute("method" ,"post" );
    document.body.appendChild( form );
    form.submit();
  }
)(location.href,prompt("please input branch name","master"))

