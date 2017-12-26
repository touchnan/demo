<%--
  Created by IntelliJ IDEA.
  User: touchnan
  Date: 2017/4/29
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //认证成功后返回票据code和自定义state状态
    //redirect_uri/?code=CODE&state=STATE。
    //code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。

    String code = request.getParameter("code");
    String state = request.getParameter("state");

    //第二步：通过票据code换取网页授权access_token
    //获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx99976c98f817fd21&secret=5e538f3c2184b404f3d4f7fabaccd9dd&code="+code+"&grant_type=authorization_code ";
    request.getRequestDispatcher(url).forward(request,response);
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
<a href="javascript:;" class="weui-btn weui-btn_primary">绿色按钮</a>
</body>
</html>

