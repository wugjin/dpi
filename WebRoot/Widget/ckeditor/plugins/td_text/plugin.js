//插件名稱
var name='td_text';
//按钮名称
var buttonName='td_text';
//函数名称
var commandName='td_text';

CKEDITOR.plugins.add(name,{
		requires:['dialog'],
		lang:['en','zh-cn'],
		init:function(editor){
			//向dialog 注入一个js
		CKEDITOR.dialog.add(commandName,this.path+"dialogs/dialogs.js");
		
			//创建命令 调用窗口
		editor.addCommand(commandName,new CKEDITOR.dialogCommand(commandName));
		//editor.addCommand(commandName,new CKEDITOR.dialog.this.path+"dialogs/111");
		// 创建命令 直接执行
		//	editor.addCommand(commandName,command);
			
			//创建一个按钮
			editor.ui.addButton(buttonName,{
				//鼠标位于按钮之上时所出现的文字提示
				//label:editor.lang.td_text.label,
				label:'td_text',
				//按钮点击之后执行的命令
				command:commandName
				//按钮的css 类名，默认：‘cke_button_’+命令名称
				//className:xxx
				//当点击按钮后所调用的函数
				//click:commandName
			});
		}
});

