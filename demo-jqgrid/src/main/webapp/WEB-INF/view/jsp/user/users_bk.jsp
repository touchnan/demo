<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/jsp/inc/head.jsp"%>

<div class="touchin-box">
  <table id="grid">
  </table>
  <div id="pager"></div>
</div>

<%@include file="/WEB-INF/view/jsp/inc/script.jsp"%>

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



        $(window).resize(function(){
            $("#listId").setGridWidth($(window).width());
        });
    });



    //-->
</script>

<%@include file="/WEB-INF/view/jsp/inc/foot.jsp"%>