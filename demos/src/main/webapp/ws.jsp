<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">

<%--
String cnt = request.getContextPath();
--%>

var socket =null;
$(function(){
	function parseObj(strData){//转换对象
	    return (new Function( "return " + strData ))();
	};
	//创建socket对象
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	//socket = new WS("ws://"+ window.location.host+"/<%-- cnt --%>/game");
	socket = new WS("ws://"+ window.location.host+"/${pageContext.request.contextPath}/game");
	//连接创建后调用
	socket.onopen = function() {
		$("#showMsg").append("连接成功...<br/>");
	};
	//接收到服务器消息后调用
	socket.onmessage = function(message) {
		var data=parseObj(message.data);
		if(data.type=="message"){
			$("#showMsg").append("<span style='display:block'>"+data.text+"</span>");
		}else if(data.type=="background"){
			$("#showMsg").append("<span style='display:block'>系统改变背景地址,背景地址是:"+data.text+"</span>");
			$("body").css("background","url("+data.text+")");
		}
	};
	//关闭连接的时候调用
	socket.onclose = function(){
		alert("close");
	};
	//出错时调用
	socket.onerror = function() {
		alert("error");
	};
	$("#sendButton").click(function() {
		socket.send($("#msg").val());
	});
	$("#abcde").click(function(){
		$.post("${pageContext.request.contextPath}/backgroundimg");
	});
});
</script>
</head>
<body>
	<div id="showMsg" style="border: 1px solid; width: 500px; height: 400px; overflow: auto;"></div>
	<div>
		<input type="text" id="msg" /> 
		<input type="button" id="sendButton" value="发送" />
		<input type="button" value="改变背景" id="abcde" />
	</div>
</body>
</html>