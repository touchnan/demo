<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/jsp/inc/head.jsp"%>

<%--<table class="datainfo">
	<caption>刷游戏币(角色列表中需重新查询，才会正确显示刷后的游戏币)</caption>
</table>--%>
<div class="search">
	<form id="dataForm" onsubmit="return false">
				<div>
					卡数:<input type="text" id="buyNum" name="buyNum" class="validate[required,custom[integer],min[-10000],max[10000]]"></input>
					<button class="all-btn blue-btn" onclick="fresh()">加卡</button>
				</div>
	</form>		
</div>
<div class="showbk-box">
</div>

<div class="touchin-box">
  <table id="grid">
  </table>
  <div id="pager"></div>
</div>

<%@include file="/WEB-INF/view/jsp/inc/script.jsp"%>

<script type="text/javascript" src="<%=staticWebRoot%>/jqvalidengine/messages.js"></script>
<script type="text/javascript" src="<%=staticWebRoot%>/jqvalidengine/jquery.validationEngine.js"></script>

<script type="text/javascript">
    <!--

    $(document).ready(function () {

       /* var userRoles = jqSelect('users/enum!userRoles.action'),
            peersites = jqSelect('//enum!peersites.action');*/

        var userRoles = "15:管理员;10:一级代理;5:二级代理";
        var proxList = jqUserSelect("<c:url value='/user/admin_prox1'/>");

        $("#grid").bindGrid({
            _oname:"user",
            url:"<c:url value='/card/listStoragers'/>",
            pager: '#pager',
            sortname: 'id',
            sortorder: "desc",
            caption:"库存管理",
            colModel:[
                {name:'id',label:'id',index:'L_id',hidedlg:true,hidden:true,editable: true},

                {name:'col_card_remain',label:'剩余卡数',index:'S_col_card_remain',align:'center',
                    hidedlg:true,
                    editable: false
                },
                {name:'col_loginname',label:'登录账号',index:'S_col_loginname',align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    editrules: { required: true }
                },
                {name:'col_realname',label:'真实姓名', index:'S_col_realname',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'col_nickname',label:'昵称', index:'S_col_nickname',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },                
                {name:'col_wxmobile',label:'手机', index:'S_col_wxmobile',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                }, 
                {name:'col_manager_id',label:'属于谁',index:'I_col_manager_id',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:proxList},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:proxList}
                },
                {name:'col_type',label:'角色',index:'I_col_type',width:60, editable:true,
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
        }).bindGridNav('#pager',{edit:false,add:false,del:false,view:true, cloneToTop:true});




        $(window).resize(function(){
            $("#listId").setGridWidth($(window).width());
        });
    });

    function fresh() {
        if ($('.showbk-box').html() === '') return alert('正处理 中，请耐心等待返回信息，不要重复提交!');
        var grid = $('#grid');
        var ids = grid.jqGrid('getGridParam', 'selarrrow')
        if (ids.length!=1) return alert('请选择单个用户');

//        for(var i=0; i<rows.length; i++){
//            var rowData = grid.jqGrid('getRowData',rows[i]);
//            //alert("rowData:"+JSON2.stringify(rowData));
//            iduuids[rowData.uuid] = rowData.id;
//        }

        var rowData = grid.jqGrid('getRowData',ids[0]);

        //$('#uuid').val(rowData.uuid);
        $('#account').val(rowData["col_loginname"]);

        if (!$("#dataForm").validationEngine('validate')) return;
        $('.showbk-box').html('');


        $(".showbk-box").load("<c:url value='/card/addCard'/>",{'buyNum':$('#buyNum').val() || 0 ,  'account': rowData["col_loginname"] || ''},function(){
            $("#grid").trigger("reloadGrid")
		});
    }


    //-->
</script>

<%@include file="/WEB-INF/view/jsp/inc/foot.jsp"%>