<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/jsp/inc/head.jsp"%>

<%--<table class="datainfo">
	<caption>刷游戏币(角色列表中需重新查询，才会正确显示刷后的游戏币)</caption>
</table>--%>

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
        var exchangeSatus="0:待支付;1:成交;-1:失败";
        var proxList = jqUserSelect("<c:url value='/user/admin_prox1'/>");

        $("#grid").bindGrid({
            _oname:"user",
            url:"<c:url value='/query/card_orders'/>",
            pager: '#pager',
            sortname: 'id',
            sortorder: "desc",
            caption:"购卡单列表",
            footerrow:true,
            //userDataOnFooter:true,
            colModel:[
                {name:'id',label:'id',index:'L_id',hidedlg:true,hidden:true,editable: true},
                {name:'status',label:'状态',index:'I_r.col_status',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:exchangeSatus},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:exchangeSatus}
                },
                {name:'col_loginname',label:'登录账号',index:'S_u.col_loginname',width:60,align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    editrules: { required: true }
                },
                {name:'col_realname',label:'真实姓名', index:'S_u.col_realname',width:60,align:'center',
                    //hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'col_nickname',label:'昵称', index:'S_u.col_nickname',align:'center',
                   // hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'}
                },
                {name:'totalCardBuy',label:'购买数',index:'S_col_card_buy',width:70,align:'center',
                   // hidedlg:true,
                    search:false,
                    editable: false
                },
                {name:'totalCardSong',label:'赠送数',index:'S_col_card_song',width:70,align:'center',
                   // hidedlg:true,
                    search:false,
                    editable: false
                },
                {name:'totalMoney',label:'人民币(元)',index:'S_col_pay_money',width:70,align:'center',
                   // hidedlg:true,
                    search:false,
                    editable: false,
                    formatter:function(cellvalue, options, rowObject) {
                        if (cellvalue) {
                            return (cellvalue/100);
                        }
                        return "";
					}
                },
                {name:'buyDate',label:'购买时间',index:'T_col_buy_time',align:'center',
                   // hidedlg:true,
                    search:true,
                    editable: false,
                    //formatter:"date",formatoptions: {srcformat:'u',newformat:'Y-m-d H:i:s'}
                    formatter: function (cellvalue, options, rowObject) {
						if (cellvalue) {
							return new Date(cellvalue).format("yyyy-MM-dd HH:mm:ss");
						}
						return "";
                    },
                    searchoptions: { sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge']}
                },
                {name:'col_manager_id',label:'属于谁',index:'I_u.col_manager_id',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:proxList},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:proxList}
                },
                {name:'col_type',label:'角色',index:'I_u.col_type',width:60, editable:true,
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
            gridComplete : function(){
                var grid = $("#grid");
                var _money= grid.jqGrid('getCol','totalMoney',false,'sum') || 0;
                var _buy= grid.jqGrid('getCol','totalCardBuy',false,'sum') || 0;
                var _song= grid.jqGrid('getCol','totalCardSong',false,'sum') || 0;
                grid.jqGrid('footerData','set',{ "col_loginname": '合计', 'totalMoney': _money*100,"totalCardBuy":_buy,"totalCardSong":_song });
            }
        }).bindGridNav('#pager',{edit:false,add:false,del:false,view:true, cloneToTop:true});




        $(window).resize(function(){
            $("#listId").setGridWidth($(window).width());
        });
    });

    //-->
</script>

<%@include file="/WEB-INF/view/jsp/inc/foot.jsp"%>