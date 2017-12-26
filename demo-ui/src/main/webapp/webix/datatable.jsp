<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/jsp/inc/inc.jsp" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">

    <title>UI-Webix</title>

    <%@ include file="/WEB-INF/view/jsp/inc/webix.jsp" %>

</head>
<body ontouchstart="">
<div id="box"></div>
<script type="text/javascript" charset="utf-8">
    webix.ui({
        rows: [
            //{ view:"template", type:"header", template:"My App!" },
            {
                view: "toolbar",
                elements: [
                    {view: "button", value: "add", width: "80"}
                ]
            },
            {
                //height:"500",
                container:"box",
                view:"datatable",
                columns:[
                    { id:"ch1", header:"", template:"{common.checkbox()}"},
                    { id:"ra1", header:"", template:"{common.radio()}"},
                    { id:"title",   header:"Film title",width:200,editor:"text" },
                    { id:"year",    header:"Release year",editor:"text"},
                    { id:"votes",   header:"Votes",editor:"text"}
                ],
//                autowidth:true,
//                autoheight:true,
                editable:true,
                url:"<c:url value='/ui?c=f&n=datatable'/>"

/*                data:[
                    { id:1, title:"The Shawshank Redemption", year:1994, votes:678790},
                    { id:2, title:"The Godfather",            year:1972, votes:511495}
                ]*/
            }
        ]
    });


</script>
</body>
</html>