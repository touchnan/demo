<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>短信400</title>
<script src="<%=contextPath%>/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
function opert(cmd) {
	var url = "<%=contextPath%>/admin?cmd="+cmd+"&t="+ (new Date().getTime());
	var options = { 
			contentType :"application/html; charset=utf-8",
			dataType: "html",
			url: url,
			beforeSend: function () {
				$("#show").text(' ').css({'border':'0px'});
			},
			complete: function () {
			},
			success : function (data) {
				$("#show").text(data);
			},
			error: function (message) {
				$("#show").text('操作失败!<BR>错误信息：' + message).css({'border':'1px solid red'});
			}
		};
		$.ajax(options);		
}
</script>
</head>
<body>
<button onclick="opert('INFO')">队列查询</button>
<button onclick="opert('SHUTDOWN')">关闭</button>
<!-- <button onclick="opert('REBOOT')">重启</button> -->
<br>
<br>
<div id="show" style="border:0px solid red;">&nbsp;</div>
</body>
</html>