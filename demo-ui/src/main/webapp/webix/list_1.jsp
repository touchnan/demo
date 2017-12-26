<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jsp/inc/inc.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">

        <title>UI-Webix</title>

        <%@ include file="/WEB-INF/view/jsp/inc/webix.jsp"%>

    </head>
    <body ontouchstart="">
        <script type="text/javascript" charset="utf-8">
    /* place for UI configuration */

			webix.ui({
                //"type":"head",
				rows:[
					{ view:"template", type:"header", template:"My App!" },
					{ view:"datatable",
						autoConfig:true,
						data:{
							title:"My Fair Lady", year:1964, votes:533848, rating:8.9, rank:5
						}
					}
				]
			});

        </script>
    </body>
</html>