<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@include file="/WEB-INF/page/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>参数设置</title>
<link rel="stylesheet" href="${ctx}/css/style.css"/>
<link rel="stylesheet" href="${ctx}/css/form.css"/>       
<link rel="stylesheet" href="${ctx}/assets/css/ace.min.css" />
<link rel="stylesheet" href="${ctx}/font/css/font-awesome.min.css" />
<link rel="stylesheet" href="${ctx}/css/validate.css" type="text/css"/>

<script src="${ctx}/assets/js/jquery.min.js"></script>
<script src="${ctx}/assets/layer/layer.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/EasyValidator/js/easy_validator.pack.js"></script>      
<script type="text/javascript">
var ctx = "${ctx}";

$(function() {
	//加载档案的字段
	getColumn();
});

function save(){
	if(batchValidate('linkForm')){
		var isIndex = 0;
		if($('#isIndex').prop("checked")){//需要建索引
			isIndex = 1;
		}
		parent.link($('#dbId').val(), $('#colId').val(), isIndex, $('#path').val());
	}
}

function cancel(){
	parent.layer.closeAll();
}

function getColumn(){
	$.ajax({
		url:ctx+'/main/get_column',
		type:"POST",
        dataType:"json",
		data:{dbId:$('#dbId').val()},
		beforeSend: function(XMLHttpRequest){ },
		success:function (data, textStatus){
			$('#colId').html('');
			if(data && data.length>0){
				for(var i=0; i<data.length; i++){
					$('#colId').append('<option value="'+data[i].colId+'">'+data[i].colCname+'</option>');
				}
			}
		},
		error:function (XMLHttpRequest, textStatus, errorThrown){
			layer.alert("对不起，更新失败！",{title: '提示框',icon:2});
		}
	});
}

</script>
</head>
<body>
	<div class="margin clearfix">
		<div style="width:750px;margin:auto;">
			<form id="linkForm">
				<table id="linkTable" border="" cellspacing="0" cellpadding="0" style="width:100%;">
					<tr>
						<td class="lable" style="width: 15%;">
							<label class="star">*</label>档案类型:
						</td>
						<td style="width: 85%;">
							<select id="dbId" name="dbId" style="width:350px;height:24px; margin-left:10px;" onChange="getColumn()">
		       				   <c:forEach items="${tableList}" var="table" begin="0" varStatus="vstatus">
								   <option value="${table.dbId}">${table.dbChin}</option>
							   </c:forEach>
		       			    </select>
						</td>
					</tr>
					<tr>
						<td class="lable">
							<label class="star">*</label>挂接字段:
						</td>
						<td style="width: 85%;">
							<select id="colId" name="colId" style="width:350px;height:24px; margin-left:10px;">
		       				   
		       			    </select>
						</td>
					</tr>
					<tr>
						<td class="lable">
							<label class="star">*</label>原文路径:
						</td>
						<td>
							<input type="text" style="width:350px;height:24px; margin-left:10px;" id="path"
								name="path" reg="notnull" tip="不能为空" value="" />
						</td>
					</tr>
					<tr>
						<td class="lable">
							<label class="star">*</label>是否建索引:
						</td>
						<td style="padding-left:5px;">
							<input id="isIndex" type="checkbox" class="ace ace-switch ace-switch-5">
							<span class="lbl middle"></span>
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div class="anniu">
			<button id="add" onclick="save()" type="button" class="btn btn-primary radius button_02"><i class="fa fa-cloud-upload"></i> 开始挂接</button>
			<button id="edit" onclick="cancel()" type="button" class="btn btn-default radius button_02"><i class="fa fa-remove"></i> 取消</button>
		</div>
	</div>
</body>
</html>