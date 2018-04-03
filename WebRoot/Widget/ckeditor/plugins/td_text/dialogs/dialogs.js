CKEDITOR.dialog.add('td_text', function(editor){
	return {
    	title:'',/*title in string*/
		minWidth:10,/*number of pixels*/
		minHeight:10,/*number of pixels*/
		resizable:CKEDITOR.DIALOG_RESIZE_NONE,/*none,width,height or both*/
		buttons:[CKEDITOR.dialog.okButton,CKEDITOR.dialog.cancelButton],/*array of button definitions*/
        contents: [{
            id: 'tb',
            name: 'tb',
            label:editor.lang.td_text.tbTip,
            title:editor.lang.td_text.tbTip,
            elements: [{
                type: 'text',
                label: editor.lang.td_text.controlnamelabel,
                id: 'controlname'
            },{
                type: 'text',
                label: editor.lang.td_text.tablenamelabel,
                id: 'tablename'
            }, {
                type: 'text',
                label: editor.lang.td_text.keylabel,
                id: 'key'
            },{
                type: 'text',
                label: editor.lang.td_text.valuelabel,
                id: 'value'
            }]
        }],
        onOk: function(){
    		var controlName=this.getValueOf('tb','controlname');
    		var tableName=this.getValueOf('tb','tablename');
    		var key=this.getValueOf('tb','key');
    		var value=this.getValueOf('tb','value');
    		if(controlName=='' || tableName=='' || key=='' || value==''){
    			alert(editor.lang.td_select.alertErrorText_gbk);
    			return true;
    		}
    		editor.insertHtml("<input type=\"text\" />");
        },
        onLoad:function(editor){
			//alert('初始化档案');
		},/*function*/
		onShow:function(editor){
		},/*function*/
		onHide:function(editor){
			//alert('隐藏');
		},/*function*/
		onCancel:function(editor){
			//alert('取消');
		}/*function*/
    };
});