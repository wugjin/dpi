<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@include file="/WEB-INF/page/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>提取首张图片</title>
<link href="${ctx}/assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx}/css/style.css"/>       
<link href="${ctx}/assets/css/codemirror.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/css/nprogress.css"/>
<link rel="stylesheet" href="${ctx}/assets/css/ace.min.css" />
<link rel="stylesheet" href="${ctx}/font/css/font-awesome.min.css" />
<link rel="stylesheet" href="${ctx}/css/form.css"/>  
<link rel="stylesheet" href="${ctx}/css/validate.css" type="text/css"/>

<script src="${ctx}/assets/js/jquery.min.js"></script>
<script src="${ctx}/assets/layer/layer.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/EasyValidator/js/easy_validator.pack.js"></script>
<script src="${ctx}/js/jqmeter.min.js" type="text/javascript" ></script>
<script type="text/javascript">
var ctx = "${ctx}";

$(function() {
	
});

function save(){
	if(batchValidate('queryForm')){
		search();
	}
}

function search(){
	//显示进度条
	showProcess();
	$.ajax({
		url:ctx+'/query/search',
		type:"POST",
        dataType:"json",
		data:{fromPath:$('#fromPath').val(),
			  toPath:$('#toPath').val()},
		beforeSend: function(XMLHttpRequest){ },
		success:function (data, textStatus){
			clearInterval(processInterval);
			layer.close(processIndex);//关闭进度条
			if(data.result=="0"){
				layer.closeAll();//关闭进度条
				
				$('#logPath').val(data.logPath);
				openResult(data);
			}else{
				layer.alert(data.errorInfo,{title: '提示框',icon:0});
			}
		},
		error:function (XMLHttpRequest, textStatus, errorThrown){
			clearInterval(processInterval);
			layer.closeAll();//关闭进度条
			layer.alert("对不起，检索失败！",{title: '提示框',icon:2});
		}
	});
}

//进度条	
var processInterval = 0;
var processIndex = -1;
function showProcess(){
	processIndex = layer.open({
		type:1,
		title: '检索进度',
	    maxmin: false, 
	    shadeClose: false, //点击遮罩关闭层
	    scrollbar: false,
	    area : ['500px' , '82px'],
	    content: '<div id="jqmeter-container"></div>',
	    cancel: function(){ 
			$.ajax({
				url:ctx+'/query/stop',
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
			url:ctx+'/query/process',
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
		html +=	'<tr><td>本次检索一共'+result.dataCount+'条</td></tr>';
		html +=	'</table>';
	
	    html +=	'<div style="width: 100%;text-align: center; margin-top:15px;">';
	    html += '<button id="edit" onclick="cancelWin()" type="button" class="btn btn-default radius button_02"><i class="fa fa-remove"></i> 关闭</button>';		
				
		html += '</div></div>';
	
	
	layer.open({
		type:1,
		title: '检索结果统计',
	    maxmin: false, 
	    shadeClose: false, //点击遮罩关闭层
	    area : ['500px' , '200px'],
	    content: html
	});
}

function cancelWin(){
	layer.closeAll();
}

</script>
</head>
<body>
	<div class="margin clearfix">
		<div style="width:750px;margin:auto;">
			<form id="queryForm">
				<table id="queryTable" border="" cellspacing="0" cellpadding="0" style="width:100%;">
					<tr>
						<td class="lable">
							<label class="star">*</label>原文路径:
						</td>
						<td>
							<input type="text" style="width:350px;height:24px; margin-left:10px;" id="fromPath"
								name="fromPath" reg="notnull" tip="不能为空" value="" />
						</td>
					</tr>
					<tr>
						<td class="lable">
							<label class="star">*</label>保存路径:
						</td>
						<td>
							<input type="text" style="width:350px;height:24px; margin-left:10px;" id="toPath"
								name="toPath" reg="notnull" tip="不能为空" value="" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div class="anniu">
			<a target="_self" href="${ctx}/index/index.html" class="btn btn-success" title="DPI帅选"><i class="fa fa-cogs"></i> DPI筛选</a>
			<button id="add" onclick="save()" type="button" class="btn btn-primary radius button_02"><i class="fa fa-cloud-upload"></i> 开始检索</button>
		</div>
	</div>
</body>
</html>