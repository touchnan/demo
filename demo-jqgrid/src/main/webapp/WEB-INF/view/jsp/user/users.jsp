<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ page isELIgnored="false"%> --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
  String ctn= request.getContextPath();
  String staticWebRoot = ctn + "/static";
%>

<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta charset="utf-8">
  <%--
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
  --%>
  <title>demo-jqgrid</title>

  <%--  <!-- The jQuery library is a prerequisite for all jqSuite products -->
    <script type="text/ecmascript" src="../../../js/jquery.min.js"></script>
    <!-- We support more than 40 localizations -->
    <script type="text/ecmascript" src="../../../js/trirand/i18n/grid.locale-en.js"></script>
    <!-- This is the Javascript file of jqGrid -->
    <script type="text/ecmascript" src="../../../js/trirand/jquery.jqGrid.min.js"></script>
    <!-- This is the localization file of the grid controlling messages, labels, etc.
    <!-- A link to a jQuery UI ThemeRoller theme, more than 22 built-in and many more custom -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <!-- The link to the CSS that the grid needs -->
    <link rel="stylesheet" type="text/css" media="screen" href="../../../css/trirand/ui.jqgrid-bootstrap.css" />--%>

  <!-- The jQuery library is a prerequisite for all jqSuite products -->
  <script type="text/javascript" src="<%=staticWebRoot%>/js/jquery-3.2.1.min.js"></script>
  <!-- We support more than 40 localizations -->
  <script type="text/javascript" src="<%=staticWebRoot%>/jqgrid/Guriddo_jqGrid_JS_5.2.1/js/i18n/grid.locale-en.js"></script>
  <!-- This is the Javascript file of jqGrid -->
  <script type="text/javascript" src="<%=staticWebRoot%>/jqgrid/Guriddo_jqGrid_JS_5.2.1/js/jquery.jqGrid.min.js"></script>

  <script type="text/javascript" src="<%=staticWebRoot%>/components/touchin/js/os.custom.js"></script>

  <!-- This is the localization file of the grid controlling messages, labels, etc.
  <!-- A link to a jQuery UI ThemeRoller theme, more than 22 built-in and many more custom -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <!-- The link to the CSS that the grid needs -->
  <link rel="stylesheet" type="text/css" media="screen" href="<%=staticWebRoot%>/jqgrid/Guriddo_jqGrid_JS_5.2.1/css/ui.jqgrid-bootstrap.css" />

  <script>
      //$.jgrid.defaults.width = 780;
      //$.jgrid.defaults.styleUI = 'Bootstrap';

  </script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body ontouchstart="">

<div class="touchin-box">
  <table id="grid">
  </table>
  <div id="pager"></div>
</div>

<%--<script type="text/javascript" src="<%=staticWebRoot%>/jquery/jquery.js"></script>--%>
<%--<script type="text/javascript" src="<%=staticWebRoot%>/jqui/i18n/jquery.ui.datepicker.js"></script>--%>
<%--<script type="text/javascript" src="<%=staticWebRoot%>/jqui/jquery.ui.js"></script>--%>
<%--<script type="text/javascript" src="<%=staticWebRoot%>/jqgrid/i18n/grid.locale.js"></script>--%>
<%--<script type="text/javascript" src="<%=staticWebRoot%>/jqgrid/jqgrid.js"></script>--%>
<%--<script type="text/javascript" src="<%=staticWebRoot%>/touchin/js/os.custom.js"></script>--%>



<script type="text/javascript">


</script>

<script type="text/javascript">
    <!--

    $(document).ready(function () {

      /* var userRoles = jqSelect('users/enum!userRoles.action'),
       peersites = jqSelect('//enum!peersites.action');*/

        var userRoles = "15:管理员;10:一级代理;5:二级代理";
        var proxList = jqUserSelect("<c:url value='/user/admin_prox1'/>");

        $("#grid").bindGrid({
            _oname:"user",
            url:"<c:url value='/user/listUsers'/>",
            editurl:"<c:url value='/user/saveUser'/>",
            pager: '#pager',
            sortname: 'id',
            sortorder: "desc",
            styleUI : 'Bootstrap',
            height: 100,
            caption:"账号管理",
            colModel:[
                {name:'id',label:'id',index:'L_id',hidedlg:true,hidden:true,editable: true},
                {name:'loginName',label:'登录账号',index:'S_loginName',align:'center',
                    //hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    editrules: { required: true }
                },
                {name:'nickname',label:'昵称', index:'S_nickname',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'wxaccount',label:'微信号', index:'S_wxaccount',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'wxmobile',label:'手机', index:'S_wxmobile',align:'center',
                    //hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'realname',label:'真实姓名', index:'S_realname',align:'center',
                    //hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'idcard',label:'身份证', index:'S_idcard',align:'center',
                    //hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'passwd',label:'密码',index:'S_passwd',hidedlg:true,hidden:true,editable:true,formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},editrules:{edithidden:true}},
                {name:'managerId',label:'属于谁',index:'I_managerId',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:proxList},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:proxList}
                },
                {name:'type',label:'角色',index:'I_type',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:userRoles},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:userRoles}
                }
            ],
            //ondblClickRow: function(id){
            //afterComplete:function(erverResponse, postdata, formid)
            //$(this).jqGrid('editGridRow',id,{ afterComplete:function(){ alert("aaaa") } });
            //$(this).trigger('jqGridAddEditClickSubmit',id);
            //},
            loadComplete:function() {

            },
        }).bindGridNav('#pager',{},{},{},
            {url:"<c:url value='/user/deleteUser'/>"}
        );



//        $(window).resize(function(){
//            $("#grid").setGridWidth($(window).width());
//        });
    });

   // alert("111")
    //$("#grid").setGridHeight(780);

    //$.jgrid.defaults.height = 780;

    //-->
</script>

<%@include file="/WEB-INF/view/jsp/inc/foot.jsp"%>