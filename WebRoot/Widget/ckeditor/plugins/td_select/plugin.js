//������Q
var name='td_select';
//��ť����
var buttonName='td_select';
//��������
var commandName='td_select';

CKEDITOR.plugins.add(name,{
		requires:['dialog'],
		lang:['en','zh-cn'],
		init:function(editor){
			//��dialog ע��һ��js
		CKEDITOR.dialog.add(commandName,this.path+"dialogs/dialogs.js");
		
			//�������� ���ô���
		editor.addCommand(commandName,new CKEDITOR.dialogCommand(commandName));
		//editor.addCommand(commandName,new CKEDITOR.dialog.this.path+"dialogs/111");
		// �������� ֱ��ִ��
		//	editor.addCommand(commandName,command);
			
			//����һ����ť
			editor.ui.addButton(buttonName,{
				//���λ�ڰ�ť֮��ʱ�����ֵ�������ʾ
				label:editor.lang.td_select.label,
				//��ť���֮��ִ�е�����
				command:commandName
				//��ť��css ������Ĭ�ϣ���cke_button_��+��������
				//className:xxx
				//�������ť�������õĺ���
				//click:commandName
			});
		}
});

