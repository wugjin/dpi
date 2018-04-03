/*
	Copyright (C) 2009 - 2012
	WebSite:	Http://wangking717.javaeye.com/
	Author:		wangking
	
	2015/3/5 新增对输入款长度校验功能，添加 len、len_min、len_chr 三种长度校验
*/
var errorTip = ""; // 记录长度校验提示信息
var xOffset = -20; // x distance from mouse
var yOffset = 20; // y distance from mouse  
$(function(){
	//input tips
    initValid();
	
	$("form").submit(function(){
		var isSubmit = true;
		$(this).find("[reg],[url]:not([reg])").each(function(){
			if($(this).attr("reg") == undefined){
				if(!ajax_validate($(this),this.tagName)){
					isSubmit = false;
				}
			}else{
				if(!validate($(this),$(this).val(),this.tagName)){
					isSubmit = false;
				}
			}
		});
		return isSubmit;
	});
	
});

function initValid(){
	$("[reg],[url]:not([reg])").hover(
			function(e) {
				//if($(this).attr('tip') != undefined){
					if($(this).attr("reg") == undefined ){
						if(ajax_validate($(this),this.tagName)){
							return;
						}
					}else{
						if(validate($(this),$(this).val(),this.tagName)){
							return;
						}
					}
					var top = (e.pageY + yOffset);
					var left = (e.pageX + xOffset);
					var tipStr  = errorTip;
					if(tipStr==""){
						tipStr = $(this).attr('tip');
					}

					$('body').append( '<p id="vtip"><img id="vtipArrow" src="'+ctx+'/images/vtip_arrow.png"/>' + tipStr + '</p>' );
					$('p#vtip').css("top", top+"px").css("left", left+"px");
				//}
			},
			function() {
				//if($(this).attr('tip') != undefined){
					$("p#vtip").remove();
				//}
			}
		).mousemove(
			function(e) {
				//if($(this).attr('tip') != undefined){
					var top = (e.pageY + yOffset);
					var left = (e.pageX + xOffset);
					$("p#vtip").css("top", top+"px").css("left", left+"px");
				//}
			}
		).blur(function(){
			if($(this).attr("reg") == undefined){
				ajax_validate($(this),this.tagName);
			}else{
				validate($(this),$(this).val(),this.tagName);
			}
		});
}

function validate(obj,objValue,tagName){
	errorTip  = "";//每次校验初始化为空
	var _reg = obj.attr("reg");
	var notnullStr = "";
	var lenStr = "";
	var regStr = "";
	var regArr = _reg.split("&&");
	
	if(regArr.length>0){
		for(var i=0; i<regArr.length; i++){
			if(regArr[i]=="notnull"){
				notnullStr = regArr[i];
			}else if(regArr[i].length>3 && regArr[i].substring(0,3)=="len"){
				lenStr = regArr[i];
			}else{
				regStr = regArr[i];
			}
		}
	}

	var isSubmit = true;
	if(notnullStr){//非空校验
		if(tagName == "SELECT"){
			if(objValue == -1){
				isSubmit = false;
				errorTip="必须选择";
			}
		}else{
			if($.trim(objValue).length == 0) {
				isSubmit = false;
				errorTip="不能为空";
			}
		}
	}
	
	if(isSubmit && regStr && objValue){//格式校验
		var reg = new RegExp(regStr);
		isSubmit = reg.test(objValue);
	}

	if(isSubmit && lenStr){//长度校验
		var index = lenStr.indexOf("[");
		var typeStr = lenStr.substring(0,index);
		var paramStr = lenStr.substring(index+1,lenStr.length-1)
		var params = paramStr.split(",") ;
		if(typeStr =="len"){
			var len=$.trim(objValue).length;
			isSubmit =  len>=parseInt(params[0])&&len<=parseInt(params[1]);
			if(!isSubmit){
				errorTip  = "输入内容长度必须介于"+params[0]+"和"+params[1]+"之间";
			}
		}else if(typeStr =="len_min"){
			isSubmit =  $.trim(objValue).length >= parseInt(params[0]);
			if(!isSubmit){
				errorTip  = "请输入至少"+params[0]+"个字符";
			}
		}else if(typeStr =="len_chr"){
			if($.trim(objValue).replace(/[^\x00-\xff]/g, "**").length > parseInt(params[0])) {
				isSubmit = false;
			}
			if(!isSubmit){
				errorTip  = "请确保输入的内容在"+params[0]+"个字符以内，一个汉字算2个字符";
			}
		}/*else if(typeStr =="len_chinese"){
			if($.trim(objValue).replace(/[^\x00-\xff]/g, "**").length > parseInt(params[0])*2) {
				isSubmit = false;
			}
			if(!isSubmit){
				errorTip  = "请确保输入的内容在"+params[0]+"个汉字以内";
			}
		}*/
	}

	if(!isSubmit){
		change_error_style(obj,tagName,"add");
		if(obj.attr("is_tip_null") == "yes"){
			obj.removeAttr("tip");
			obj.removeAttr("tip_bak");
		}else{
			obj.attr("tip",obj.attr("tip_bak"));
			obj.removeAttr("tip_bak");
		}
		return false;
	}else{
		if(obj.attr("url") == undefined){
			obj.attr("tip",obj.attr("tip_bak"));
			obj.removeAttr("tip_bak");
			change_error_style(obj,tagName,"remove");
			return true;
		}else{
			return ajax_validate(obj);
		}
	}
}

function ajax_validate(obj,tagName){
	if(obj.attr("tip") == undefined){
		obj.attr("is_tip_null","yes");
	}
	var url_str = obj.attr("url");
	if(url_str.indexOf("?") != -1){
		url_str = url_str+"&"+obj.attr("name")+"="+obj.attr("value");
	}else{
		url_str = url_str+"?"+obj.attr("name")+"="+obj.attr("value");
	}
	var feed_back = $.ajax({url: url_str,cache: false,async: false}).responseText;
	feed_back = feed_back.replace(/(^\s*)|(\s*$)/g, "");
	if(feed_back == 'success'){
		change_error_style(obj,tagName,"remove");
		if(obj.attr("is_tip_null") == "yes"){
			obj.removeAttr("tip");
			obj.removeAttr("tip_bak");
		}else{
			obj.attr("tip",obj.attr("tip_bak"));
			obj.removeAttr("tip_bak");
		}
		return true;
	}else{
		change_error_style(obj,tagName,"add");
		if(obj.attr("tip_bak") == undefined){
			obj.attr("tip_bak",obj.attr("tip"));
			obj.attr("tip",feed_back);
		}
		return false;
	}
}

function change_error_style(obj,tagName,action_type){
	if(action_type == "add"){
		if(tagName == "SELECT"){
			obj.addClass("select_validation-failed");
		}else{
			obj.addClass("input_validation-failed");
		}
	}else{
		if(tagName == "SELECT"){
			obj.removeClass("select_validation-failed");
		}else{
			obj.removeClass("input_validation-failed");
		}
	}
}

/**
 *批量校验 
 */
function batchValidate(formId){
	var isSubmit = true;
	$("#"+formId).find("[reg],[url]:not([reg])").each(function(){
		if($(this).attr("reg") == undefined){
			if(!ajax_validate($(this),this.tagName)){
				isSubmit = false;
			}
		}else{
			if(!validate($(this),$(this).val(),this.tagName)){
				isSubmit = false;
			}
		}
	});
	return isSubmit;
}
/**
 * 单个输入框校验
 */
function singleValidate(Id){
	var isSubmit = true;
	if($("#"+Id).attr("reg") == undefined){
		if(!ajax_validate($("#"+Id),$("#"+Id)[0].tagName)){
			isSubmit = false;
		}
	}else{
		if(!validate($("#"+Id),$("#"+Id).val(),$("#"+Id)[0].tagName)){
			isSubmit = false;
		}
	}
	return isSubmit;
}