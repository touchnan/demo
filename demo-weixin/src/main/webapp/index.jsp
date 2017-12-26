<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<% 
	String ctn=request.getContextPath();
	
	
		String ip = request.getHeader("x-forwarded-for");
		String ip_type = "x-forwarded-for";
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			ip_type = "Proxy-Client-IP";
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			ip_type = "WL-Proxy-Client-IP";
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			ip_type = "RemoteAddr";
		}

%> 

<html>
  <head>
<meta http-equiv="content-type" content="text/html;charset=utf8"/>
<meta id="viewport" name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1; user-scalable=no;" />
<title>【微信支付V2.0】MWEB支付实例</title>
<link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/1.1.2/weui.min.css"/>
<script type="text/javascript" src="zepto.min.js"></script>


  </head>
  <body>

<article class="charge">
		<h1>得意在线-微信支付-H5-demo</h1>
		<section class="content">
				<h2>商品：得意在线-测试商品。</h2>		
		  <ul class="select cf">
					<li>><%=ip_type%>:<%=ip%></li>
				</ul>
				<p class="copy-right">亲，此商品不提供退款和发货服务哦</p>
				<div class="price">微信价：<strong>￥0.01元</strong></div>
				<div class="operation"><a class="btn-green weui-btn weui-btn_mini weui-btn_primary" id="getBrandWCPayRequest" onClick="buy()">立即购买</a></div>
				<p class="copy-right">微信支付demo 由腾讯微信提供</p>
		</section>
	</article>

    <script type="text/javascript">
	function buy() {
		ajax();
	}
	
    function ajax(options) {
        var _options = {
            type: 'GET',
            url: '<%= ctn%>'+'/wx/pay',
            data: {},
            contentType:"application/x-www-form-urlencoded",
            complete:function (xhr, status) {
            },
             success: function(data){
             	if (data && data.mweb_url) {
             		window.location.href=data.mweb_url;
             	} else {
             	  alert("succ,error")
             	  alert(data);
             	  console.log(data)
             	}
            },
            error:function (xhr, errorType, error) {
               alert("error")
               alert(error)
               console.log(error)
            },
            timeout:0,
            async:true,
            dataType: "json"
        };
        $.ajax($.extend(_options, options));
    }    
    
    
    </script>

  </body>
</html>
