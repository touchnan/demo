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

        var statusOptions = "0:待发布;1:发布;-1:取消";

        $("#grid").bindGrid({
            _oname:"card",
            url:"<c:url value='/card/listCards'/>",
            editurl:"<c:url value='/card/saveCard'/>",
            pager: '#pager',
            sortname: 'id',
            sortorder: "desc",
            caption:"卡活动管理",
            colModel:[
                {name:'id',label:'id',index:'L_id',hidedlg:true,hidden:true,editable: true},
                {name:'uuid',label:'uuid',index:'S_uuid',hidedlg:true,hidden:true,editable: true},

                {name:'name',label:'活动名称',index:'S_name',align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    //formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    editrules: { required: true }
                },
                {name:'css',label:'样式（颜色）',index:'S_css',align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    //editrules: { required: true },
                    search:false
                },
                {name:'cardBuy',label:'买卡数',index:'I_cardBuy',align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    editrules: { required: true }
                },
                {name:'cardSong',label:'赠卡数',index:'I_cardSong',align:'center',
                    hidedlg:true,
                    editable: true,
                    //formatter: 'integer',
                    //editoptions: { size: 10, readonly: 'readonly'},
                    formoptions:{elmprefix:'(*)'},
                    editrules: { required: true }
                },
                {name:'remark',label:'活动说明', index:'S_remark',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'&nbsp;&nbsp;&nbsp;&nbsp;'},
                    //formoptions:{elmprefix:'(*)'},
                    //editrules: { required: true }
                },
                {name:'sort',label:'排序', index:'I_sort',align:'center',
                    hidedlg:true,
                    editable:true,
                    formoptions:{elmprefix:'(*)'},
                    //editoptions:{maxlength:2,},
                    editrules: { required: true }
                },
                {name:'status',label:'状态',index:'I_status',width:60, editable:true,
                    edittype:'select',
                    formatter:'select',
                    editoptions:{value:statusOptions},
                    formoptions:{elmprefix:'(*)'},
                    search:true,
                    stype:'select',
                    searchoptions:{value:statusOptions}
                }
            ]
        }).bindGridNav('#pager',{},{},{},
            {url:"<c:url value='/card/deleteCard'/>"}
        );



        $(window).resize(function(){
            $("#listId").setGridWidth($(window).width());
        });
    });



    //-->
</script>

<%@include file="/WEB-INF/view/jsp/inc/foot.jsp"%>