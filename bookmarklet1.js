/* 任意のページでこのブックマークレットクリックすると
 * promptがでてくるので、そこにgithubの
 *
 * ユーザ名 / プロジェクト名
 *
 * を入力すると、シンタックスハイライトされたソースファイル一式がdownloadされる
 * (ファイルが大き過ぎて、タイムアウトする場合あり)
 *
 * メールも設定しておけば、添付ファイルで送られてくる・・・はず
 * ちなみに、メールの添付ファイルがzipの拡張子だとGAEの仕様上送れなったので、
 * 拡張子偽装して送るようになってるw
 */

javascript:(
(function(url){
  if((url==null)||(url==""))return;
  (function(branch){
    if((branch==null)||(branch==""))return;
    var form = document.createElement("form");
    var add  = function(name,value){
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
    add("url","http://github.com/" + url + "/zipball/" + branch );
    form.setAttribute("action" ,"http://syntax-highlight.appspot.com/source.zip" );
    form.setAttribute("method" ,"post" );
    document.body.appendChild( form );
    form.submit();
  })(prompt("please input branch name","master"))
})(prompt("please input project name"))
)

