/**
 * ブラウザで、あるgithubのプロジェクトのトップページを開いてる状態のときに
 * このブックマークレットをクリックするだけで
 * そのプロジェクトの master branch をダウンロードするやつ
 *
 * master branch が存在しないとエラーになる。
 *
 * そもそも master 固定にしちゃってるけど、
 * プロンプト表示して、branch選べるようにするなり、勝手に改造してください
 */

javascript: (
  function(url){
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
  add("url", url   "/zipball/master" );
  form.setAttribute("action" ,"http://syntax-highlight.appspot.com/source.zip" );
  form.setAttribute("method" ,"post" );
  document.body.appendChild( form );
  form.submit();
})(location.href)

