<%--
  Created by IntelliJ IDEA.
  User: chengqiang.han
  Date: 2017/5/2
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String nickname = (String) request.getSession().getAttribute("nickname");
    String unionid = (String) request.getSession().getAttribute("unionid");
    Integer sex = (Integer) request.getSession().getAttribute("sex");
    String province = (String) request.getSession().getAttribute("province");
    String city = (String) request.getSession().getAttribute("city");
    String country = (String) request.getSession().getAttribute("country");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>WeUI</title>

    <!-- 引入 WeUI -->
    <%--<link rel="stylesheet" href="https://res.wx.qq.com/open/libs/weui/0.4.3/weui.min.css"/>--%>
    <link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css"/>
</head>
<body>
<!-- 使用 -->
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= nickname %></a>
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= unionid %></a>
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= sex %></a>
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= province %></a>
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= city %></a>
<a href="javascript:;" class="weui-btn weui-btn_primary"><%= country %></a>

</body>
</html>
