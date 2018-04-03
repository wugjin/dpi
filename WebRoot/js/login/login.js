$(function () {
    //密码错误的提示信息
    if($("#errorInfo").val()!=""){
		 if($('#password').val()==''){
	    		layer.tips($("#errorInfo").val(), '#password', {
	    			tips: [1, '#3595CC'],
	    			time: 4000
	    		});
	    		$("#password").focus();
	     }
    }
    
    //按回车键触发登录事件
    document.onkeydown = function() {
		if(event.keyCode == 13) {
			login();
		}
	};
});

function login(){
	if(!$('#password').val()){
		layer.tips('请输入密码!', '#password', {
			tips: [1, '#3595CC'],
			time: 4000
		});
		$("#password").focus();
		return ;
	}else{
		loginForm.submit();
	}
}

