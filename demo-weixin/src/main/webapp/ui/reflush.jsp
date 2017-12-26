<%--
  Created by IntelliJ IDEA.
  User: <a href="mailto:88052350@qq.com">chengqiang.han</a>
  Date: 2017/11/7
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<%@ include file="/WEB-INF/view/jsp/inc/wxhead.jsp" %>--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

    $(function(){
        // 页数
        var page = 0;
        // 每页展示5个
        var size = 5;

        // dropload
        $('#content').dropload({
            scrollArea : window,
            domUp : {
                domClass   : 'dropload-up',
                domRefresh : '<div class="dropload-refresh">↓下拉刷新-自定义内容</div>',
                domUpdate  : '<div class="dropload-update">↑释放更新-自定义内容</div>',
                domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中1-自定义内容...</div>'
            },
            domDown : {
                domClass   : 'dropload-down',
                domRefresh : '<div class="dropload-refresh">↑上拉加载更多-自定义内容</div>',
                domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中2-自定义内容...</div>',
                domNoData  : '<div class="dropload-noData">暂无数据-自定义内容</div>'
            },

//            loadUpFn : function(me){
//                $.ajax({
//                    type: 'GET',
//                    url: 'json/update.json',
//                    dataType: 'json',
//                    success: function(data){
//                        var result = '';
//                        for(var i = 0; i < data.lists.length; i++){
//                            result +=   '<a class="item opacity" href="'+data.lists[i].link+'">'
//                                +'<img src="'+data.lists[i].pic+'" alt="">'
//                                +'<h3>'+data.lists[i].title+'</h3>'
//                                +'<span class="date">'+data.lists[i].date+'</span>'
//                                +'</a>';
//                        }
//                        // 为了测试，延迟1秒加载
//                        setTimeout(function(){
//                            $('.lists').html(result);
//                            // 每次数据加载完，必须重置
//                            me.resetload();
//                            // 重置页数，重新获取loadDownFn的数据
//                            page = 0;
//                            // 解锁loadDownFn里锁定的情况
//                            me.unlock();
//                            me.noData(false);
//                        },1000);
//                    },
//                    error: function(xhr, type){
//                        alert('Ajax error!');
//                        // 即使加载出错，也得重置
//                        me.resetload();
//                    }
//                });
//            },

            loadDownFn : function(me){
                page++;
                // 拼接HTML
                var result = '';
                $.ajax({
                    type: 'GET',
                    url: 'http://ons.me/tools/dropload/json.php?page='+page+'&size='+size,
                    dataType: 'json',
                    success: function(data){
                        var arrLen = data.length;
                        if(arrLen > 0){
                            var r = [];
                            for (var i=0;i<5;i++) {
                                r.push("<div>"+i+"Hello world!</div>");

                            }
                            $(".lists").append(r.join());
                            console.log("======");
                        }else{
                            // 锁定
                            me.lock();
                            // 无数据
                            me.noData();
                        }

                        me.resetload();// 每次数据插入，必须重置
                    },
                    error: function(xhr, type){
                        alert('Ajax error!');
                        // 即使加载出错，也得重置
                        me.resetload();
                    }
                });
            }
        });
    });

    //-->
</script>
