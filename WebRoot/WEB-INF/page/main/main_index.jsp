<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@include file="/WEB-INF/page/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>原文批量挂接</title>
<link href="${ctx}/assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx}/css/style.css"/>       
<link href="${ctx}/assets/css/codemirror.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/css/nprogress.css"/>
<link rel="stylesheet" href="${ctx}/assets/css/ace.min.css" />
<link rel="stylesheet" href="${ctx}/font/css/font-awesome.min.css" />
<link rel="stylesheet" href="${ctx}/css/validate.css" type="text/css"/>
<!--[if lte IE 8]>
  <link rel="stylesheet" href="${ctx}/assets/css/ace-ie.min.css" />
<![endif]-->
<script src="${ctx}/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/assets/js/bootstrap.min.js"></script>
<script src="${ctx}/assets/js/typeahead-bs2.min.js"></script>           	
<script src="${ctx}/assets/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${ctx}/assets/layer/layer.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/EasyValidator/js/easy_validator.pack.js"></script>
<script src="${ctx}/js/jqmeter.min.js" type="text/javascript" ></script>
<script type="text/javascript">
var ctx = "${ctx}";
var pageSize = "${pageSize}";
var rowIndex = 1;
	$(function() {
		$('#itemTable').dataTable( {
                "ordering": false,
                "searching": false,
                "lengthChange": false,
                "pageLength": pageSize,
                "scrollY": ($(window).height()-210)+"px",
                "serverSide": true,
                ajax : {  
                    url: ctx+"/main/query",
                    // 传入已封装的参数  
                    data: function(data){
                    	data = setParam(data);
                    }
                },  
                "rowId":"fileId",
                "columns": [
					{ "title": "序号","width":"5%","class": "center","targets": 0},
					{ "title": "文件名", "width":"20%", "data":"fileName", "class": "td_left", "targets": 1},
		            { "title": "大小", "width":"8%", "data":"fileLength", "class": "center", "targets": 2},
                    { "title": "存储路径","width":"47%", "data":"filePath", "class": "center", "targets": 3},
                    { "title": "所属档案","width":"20%", "data":"dbId", "class": "center", "targets": 4}
                ],
                "columnDefs": [  
                               {  
                                  "render": function ( data, type, row ) {
                                	 return rowIndex++;
                                   },  
                                   "targets": 0  
                               },
                               {  
                                   "render": function ( data, type, row ) {
                                   	if(row.fileType){
                                   		return data+"."+row.fileType;
                                   	}
        	                        	return data;
                                    },  
                                    "targets": 1  
                                },
                               {  
                                   "render": function ( data, type, row ) {  
                                   	var fileSizeStr = "";
                                   	fileSize = Number(data);
                                   	if(fileSize<1024){
                                   		fileSizeStr = fileSize+"B";
                                   	}else{
                                   		fileSize = Number(fileSize)/1024;
                                   		fileSizeStr = Math.round(fileSize)+"K";
                                   		if(fileSize >= 1024){
                                   			fileSize = fileSize/1024;
                                   			fileSizeStr = Math.round(fileSize*100)/100+"M";
                                   		}
                                   	}
                                   	return fileSizeStr;
                                    },  
                                    "targets": 2  
                                }
                              ]
            }).on('init.dt', function ( e, settings, json, xhr ) {
            	
            });
		
		$(window).resize(function(){
			$(".dataTables_scrollBody").css("height",($(window).height()-210)+"px");
		});
		
	});
	
	//设置查询条件
	function setParam(data){
		rowIndex=1;
		if($('#dbId').val()){
			data.dbId = $('#dbId').val();
		}
		if($('#name').val()){
			data.name = $('#name').val();
		}
		
		return data;
	}

	function queryTable(){
		rowIndex=1;
		$('#itemTable').DataTable().ajax.reload(null, true);
	}
	
	function toLinkPage(){
		layer.open({
			type:2,
			title: '挂接设置',
		    maxmin: false, 
		    shadeClose: false, //点击遮罩关闭层
		    area : ['550px' , '350px'],
		    content: [ctx+'/main/set.html', 'no']
		});
	}
	
	function link(dbId, colId, isIndex, path){
		//显示进度条
		showProcess();
		$.ajax({
			url:ctx+'/main/link',
			type:"POST",
            dataType:"json",
			data:{dbId:dbId,
				  colId:colId,
				  isIndex:isIndex,
				  path:path},
			beforeSend: function(XMLHttpRequest){ },
			success:function (data, textStatus){
				clearInterval(processInterval);
				layer.close(processIndex);//关闭进度条
				if(data.result=="0"){
					layer.closeAll();//关闭进度条
					queryTable();
					
					$('#logPath').val(data.logPath);
					openResult(data);
				}else{
					layer.alert(data.errorInfo,{title: '提示框',icon:0});
				}
			},
			error:function (XMLHttpRequest, textStatus, errorThrown){
				clearInterval(processInterval);
				layer.closeAll();//关闭进度条
				layer.alert("对不起，更新失败！",{title: '提示框',icon:2});
			}
		});
	}
	
	//进度条	
	var processInterval = 0;
	var processIndex = -1;
	function showProcess(){
		processIndex = layer.open({
			type:1,
			title: '导入进度',
		    maxmin: false, 
		    shadeClose: false, //点击遮罩关闭层
		    scrollbar: false,
		    area : ['500px' , '82px'],
		    content: '<div id="jqmeter-container"></div>',
		    cancel: function(){ 
				$.ajax({
					url:ctx+'/main/stop',
					type:"POST",
					dataType:"json",
					data:{},
					success:function (data){},
					error:function (XMLHttpRequest, textStatus, errorThrown){
						layer.alert('对不起，您的请求失败！',{title: '提示框',icon:2});
					}
				});
		    }
		});
		
		setProcess(0);
		
		processInterval = setInterval(function(){
			$.ajax({
				url:ctx+'/main/process',
				type:"POST",
				dataType:"json",
				data:{},
				success:function (data){
					setProcess(data.process);
				},
				error:function (XMLHttpRequest, textStatus, errorThrown){
					//layer.alert('对不起，您的请求失败！',{title: '提示框',icon:2});
				}
			});
		},2000);
	}

	var preProcess = 0;
	function setProcess(process){
		if(process != preProcess){
			$('#jqmeter-container').jQMeter({
			    goal:'$100',
			    raised:'$'+process,
			    width:'500px',
			    height:'40px',
			    animationSpeed: 0,
			    counterSpeed: 500
			});
		}
	}
	
	
	function openResult(result){
		var html  = '<div class="margin clearfix">';
			html +=	'<table cellspacing="0" cellpadding="0" class="table table-striped table-bordered table-hover dataTable no-footer">';
			html +=	'<tr><td>本次挂接一共'+result.totalCount+'条</td></tr>';
			html +=	'<tr><td>成功导入'+result.successCount+'条</td></tr>';
			html +=	'<tr><td>失败'+result.failCount+'条</td></tr>';
			html +=	'</table>';
		
		    html +=	'<div style="width: 100%;text-align: center; margin-top:15px;">';
		    if(result.failCount>0){
		    	html += '<button id="add" onclick="dowloadLog()" type="button" class="btn btn-primary radius button_02"><i class="fa fa-arrow-circle-o-down"></i> 下载错误日志</button>';
		    }
		    html += '<button id="edit" onclick="cancelWin()" type="button" class="btn btn-default radius button_02"><i class="fa fa-remove"></i> 关闭</button>';		
					
			html += '</div></div>';
		
		
		layer.open({
			type:1,
			title: '导入结果统计',
		    maxmin: false, 
		    shadeClose: false, //点击遮罩关闭层
		    area : ['500px' , '230px'],
		    content: html
		});
	}
	
	function cancelWin(){
		layer.closeAll();
	}
	
	function dowloadLog(){
		document.AnnexForm.method = "post";
		document.AnnexForm.submit();
	}
</script>
</head>
<body>
	<div class="margin clearfix">
   <div class="border clearfix">
       <span class="l_f" style="margin-left:20px;">
       		档案类型：<select id="dbId" name="dbId" style="width:200px;height:24px; margin-left:10px;">
       				  <option value="">--------请选择--------</option>
       				  <c:forEach items="${tableList}" var="table" begin="0" varStatus="vstatus">
						  <option value="${table.dbId}">${table.dbChin}</option>
					  </c:forEach>
       			   </select>&nbsp;&nbsp;
        	文件名称：<input id="name" name="name" type="text" value="" style="width:200px;height:24px; margin-left:10px;"/>&nbsp;&nbsp;
	        <a href="javascript:void(0)" onclick="queryTable()" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
        </span>
        
        <span class="r_f" style="margin-left:50px;">
	        <a href="javascript:void(0)" onclick="toLinkPage()" class="btn btn-primary"><i class="fa fa-cog"></i> 挂接设置</a>
        </span>
     </div>
     <div class="compete_list">
     	<table class="table table-striped table-bordered table-hover" id="itemTable"></table>
     </div>
 </div>
 <form name="AnnexForm" action="${ctx}/main/download_log">
	<input type="hidden" id="logPath" name="logPath"/>
</form>
</body>
</html>