<%--
  Created by IntelliJ IDEA.
  User: <a href="mailto:88052350@qq.com">chengqiang.han</a>
  Date: 2017/11/8
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/inc/inc.jsp" %>

<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">

    <%-- <meta name="viewport" content="initial-scale=1, user-scalable=0, minimal-ui" charset="UTF-8">--%>
    <!-- UC强制全屏 -->
    <meta name="full-screen" content="yes">
    <!-- QQ强制全屏 -->
    <meta name="x5-fullscreen" content="true">

    <title>WeUI 应用案例</title>

    <link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css"/>
    <!--
    <link rel="stylesheet" href="https://res.wx.qq.com/open/libs/weui/0.4.3/weui.min.css"/>
    -->


    <link rel="stylesheet" href="<c:url value='/static/dropload/dropload.css' />"/>


</head>

<body ontouchstart="">


<div id="content">
    <div class="lists"></div>
</div>


<%@ include file="/inc/wxscript.jsp" %>
<script type="text/javascript" src="<c:url value='/static/dropload/dropload.min.js' />"></script>


<script type="text/javascript">
    <!--

    var a = function () {
        return document.documentElement.scrollTop || document.body.scrollTop
    }
    (0,2)(this).on();

    $(function(){

    });

    //-->
</script>

<%@ include file="/inc/wxfoot.jsp" %>

