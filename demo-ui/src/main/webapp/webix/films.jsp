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
<script type="text/javascript" charset="utf-8">
    webix.ui({
        rows: [
            //{ view:"template", type:"header", template:"My App!" },
            {
                view: "toolbar",
                elements: [
                    {view: "button", value: "add", width: "80",click:add_row},
                    {view: "button", value: "update", width: "80",click:update_row},
                    {view: "button", value: "delete", width: "80",click:delete_row},
                    {view: "button", value: "clear", width: "80",click:clear_form},
                    {view: "button", value: "binding", width: "80",click:"$$('myform').save()"}
                ]
            },
            {
                height:"500",
                cols: [
                    {
                        view: "form",
                        id:"myform",
                        elements: [
                            {view:"text",name:"title",placeholder:"title"},
                            {view:"text",name:"year",placeholder:"year"}
                        ]
                    },
                    {
                        view:"list",
                        id:"mylist",
                        template:"#title# #year#",
                        select:true,
                        url:"<c:url value='/ui?c=f&n=films'/>",
                        datatype:"json"
//                        data:[
//                            {id:1,title:"Who am I",year:"1982",bird:"aaa"},
//                            {id:2,title:"Kongfu Panda!",year:"2008",bird:"222"}
//                        ]
                    }
                ]
            }
        ]
    });

    $$("myform").bind($$("mylist"));

    <%--var dp = new webix.DataProcessor({--%>
        <%--master: $$('mylist'),  // specifies the desired component--%>
        <%--url: "<c:url value='/ui?c=f&n=films'/>"  // specifies the server API endpoint--%>
    <%--});--%>


/*    $$("mylist").attachEvent("onAfterSelect", function(id){
        var obj = $$("mylist").getItem(id);
        $$("myform").setValues({
            title: obj.title,
            year: obj.year
        });
    });*/

    function add_row() {
        $$("mylist").add({
            title: $$("myform").getValues().title,
            year: $$("myform").getValues().year,
        })
    }
    function update_row() {
        var sel = $$("mylist").getSelectedId(); //checks whether the item is selected
        if(!sel) return; // if not, function execution is stopped

        var value1 = $$('myform').getValues().title;
        var value2 = $$('myform').getValues().year;

        var item = $$("mylist").getItem(sel); //selected item object
        item.title = value1;
        item.year = value2;
        $$("mylist").updateItem(sel, item);
    }
    function delete_row() {
         var id = $$("mylist").getSelectedId(); //returns the ID of selected item
         $$("mylist").remove(id);
    }
    
    function clear_form() {
        $$('myform').clear();
    }

</script>
</body>
</html>