<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jsp/inc/inc.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">

        <title>UI-Webix</title>

        <%@ include file="/WEB-INF/view/jsp/inc/webix.jsp"%>

		<script type="text/javascript" data-main="app" src="<c:url value='/static/requirejs-2.3.3/require.js'/>" ></script>

		<script type="text/javascript">
//			require.config({
//				paths: { text:"libs/text" }
//			});
		</script>

    </head>
    
    <body ontouchstart="">
    </body>
</html>