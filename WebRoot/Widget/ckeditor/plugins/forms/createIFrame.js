
	function createIframe(){  
		//mask���ֲ�
		var newMask=document.createElement("div");
		newMask.id="mDiv";
		newMask.style.position="absolute";
		newMask.style.zIndex="1";
		_scrollWidth=Math.max(document.body.scrollWidth,document.documentElement.scrollWidth);
		_scrollHeight=Math.max(document.body.scrollHeight,document.documentElement.scrollHeight);
		// _scrollHeight = Math.max(document.body.offsetHeight,document.documentElement.scrollHeight);
		newMask.style.width=_scrollWidth+"px";
		newMask.style.height=_scrollHeight+"px";
		newMask.style.top="0px";
		newMask.style.left="0px";
		newMask.style.background="#33393C";
		//newMask.style.background = "#FFFFFF";
		newMask.style.filter="alpha(opacity=40)";
		newMask.style.opacity="0.40";
		newMask.style.display='none';
		
		var objDiv=document.createElement("DIV");
		objDiv.id="div1";
		objDiv.name="div1";
		objDiv.style.width="480px";
		objDiv.style.height="200px";
		objDiv.style.left=(_scrollWidth-480)/2+"px";
		objDiv.style.top=(_scrollHeight-200)/2+"px";
		objDiv.style.position="absolute";
		objDiv.style.zIndex="2"; //������������objDiv����newMask֮��
		objDiv.style.display="none"; //��objDivԤ������
		objDiv.innerHTML=' <div id="drag" style="position:absolute;height:25px;width:100%;z-index:10001;top:0; background-color:#0033FF;cursor:move ;" align="right"> <input type=button value="X" onclick="HideIframe(document.getElementById(\'mDiv\'),document.getElementById(\'div1\'));"/> </div>';
		//������X��ťΪ�����ر��¼���
		objDiv.style.border="solid #0033FF 3px;";
		var frm=document.createElement("iframe");
		frm.id="ifrm";
		frm.name="ifrm";
		frm.style.position="absolute";
		frm.style.width="100%";
		frm.style.height=160;
		frm.style.top=20;
		frm.style.display='';
		frm.frameborder=0;
		objDiv.appendChild(frm);
		
		//�ύ��ť
		var submitButtonDiv =document.createElement("div");
		submitButtonDiv.setAttribute("style", "position:absolute;height:25px;width:100%;z-index:10001;top:175px; background-color:#0033FF;cursor:move ;");
		submitButtonDiv.align="center";
		submitButtonDiv.innerHTML="<input id=btGo type=button value='��ʾ'/>";
		objDiv.appendChild(submitButtonDiv);
	
		// newMask.appendChild(objDiv); //�������������frame���ڵ�div����� newMask����Ԫ�أ���newMask͸���ȸ���ʱ����Ȼ��Ӱ�쵽frame
		document.body.appendChild(newMask);
		document.body.appendChild(objDiv);
		
		var objDrag=document.getElementById("drag");
		var drag=false;
		var dragX=0;
		var dragY=0;

		//��IE�����
		//objDrag.addEventListener("onmousedown",startDrag);
		objDrag.attachEvent("onmousedown",startDrag);
		function startDrag(){
			if(event.button==1&&event.srcElement.tagName.toUpperCase()=="DIV"){
				objDrag.setCapture();
				objDrag.style.background="#0000CC";
				drag=true;
				dragX=event.clientX;
				dragY=event.clientY;
			}
		};
		//�ǣɣ��������
		//objDrag.addEventListener("onmousemove",Drag);
		objDrag.attachEvent("onmousemove",Drag);
		function Drag(){
			if(drag){
				var oldwin=objDrag.parentNode;
				oldwin.style.left=oldwin.offsetLeft+event.clientX-dragX;
				oldwin.style.top=oldwin.offsetTop+event.clientY-dragY;
				oldwin.style.left=event.clientX-100;
				oldwin.style.top=event.clientY-10;
				dragX=event.clientX;
				dragY=event.clientY;
			}	
		};
		//�ǣɣ��������
		//objDrag.addEventListener("onmouseup",stopDrag);
		objDrag.attachEvent("onmouseup",stopDrag);
		function stopDrag(){
				objDrag.style.background="#0033FF";
				objDrag.releaseCapture();
				drag=false;
		};
	}
	//select ���
	function htmlEditorSelect(id,ckeditor){
		var columnNameTag="sel_colum";
		var columnNameTag="columnName";
		var linkTableNameTag="linkTableName";
		var linkColumnKeyTag="linkColumnKey";
		var linkColumnValueTag="linkColumnValue";
		
	    var frm=document.getElementById("ifrm");
		var objDiv=document.getElementById("div1");
		var mDiv=document.getElementById("mDiv");
		mDiv.style.display='';
		/*
		var iframeTextContent='';
		iframeTextContent+=' <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">';
		iframeTextContent+=' <html xmlns="http://www.w3.org/1999/xhtml">';
		iframeTextContent+=' <head>';
		iframeTextContent+=' <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />';
		iframeTextContent+=' </head>';
		iframeTextContent+=' <body>';
		iframeTextContent+=' <table>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <td>controlName</td>';
		iframeTextContent+=' <td> <input type="text"  id="select_controlName" value="" /> </td>';
		iframeTextContent+=' </tr>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <td>tableName</td>';
		iframeTextContent+=' <td> <input type="text"  id="select_tableName" value="" /> </td>';
		iframeTextContent+=' </tr>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <td>key</td>';
		iframeTextContent+=' <td> <input type="text" id="select_key" value="" /> </td>';
		iframeTextContent+=' </tr>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <td>value</td>';
		iframeTextContent+=' <td> <input type="text" id="select_value" value="" /> </td>';
		iframeTextContent+=' </tr>';
		iframeTextContent+=' <tr>';
		iframeTextContent+=' <td> <input type="button" id="btGo" value="Go" /> </td>';
		iframeTextContent+=' </tr>';
		iframeTextContent+=' </table>';
		iframeTextContent+=' </body>';
		iframeTextContent+=' </html>';	
		frm.contentWindow.document.designMode='off';
		frm.contentWindow.document.open();
		frm.contentWindow.document.write(iframeTextContent);
		frm.contentWindow.document.close();
		*/
		frm.setAttribute("src", getRootPath()+"ckeditorplus/SelectServlet");
		objDiv.style.display = ""; //��ʾ������div
		
		var objGo=document.getElementById("btGo");
		objGo.onclick=function (){
			var columnName=frm.contentWindow.document.getElementById(columnNameTag).value;
			var linkTableName=frm.contentWindow.document.getElementById(linkTableNameTag).value;
			var linkColumnKey=frm.contentWindow.document.getElementById(linkColumnKeyTag).value;
			var linkColumnValue=frm.contentWindow.document.getElementById(linkColumnValueTag).value;
		
			var htmlstr="<select datafld=\"SYS_SEL\" id=\""+columnName
			+"\" name=\""+columnName+"\"" +" sel_table=\""
			+linkTableName+"\" sel_key=\""+linkColumnKey+"\" sel_value=\""
			+linkColumnValue+"\"  sel_colum=\""+columnName+"\"></select>";
			ckeditor.insertHtml(htmlstr);
			HideIframe(mDiv,objDiv);
		};
		/*��ie�����
		objGo.addEventListener("onclick",function (){
			HideIframe(mDiv,objDiv);
		});*/
	}
	
	//text���
	function htmlEditorText(id,ckeditor){
		//�б�ǩ
		var columnNameTag="text_colum";
		
		//���� textServlet ����ʾ����
	    var frm=document.getElementById("ifrm");
		var objDiv=document.getElementById("div1");
		var mDiv=document.getElementById("mDiv");
		mDiv.style.display='';
		frm.setAttribute("src", getRootPath()+"ckeditorplus/TextServlet");
		objDiv.style.display = ""; //��ʾ������div
		
		var objGo=document.getElementById("btGo");
		objGo.onclick=function (){
			var controlName=frm.contentWindow.document.getElementById(columnNameTag).value;
			var htmlstr="<input datafld=\"SYS_TEXT\" type='text'  id=\""+controlName
			+"\" name=\""+controlName+"\"  "+columnNameTag+"=\""+controlName+"\" /> " ;
			ckeditor.insertHtml(htmlstr);
			HideIframe(mDiv,objDiv);
		};
		/*��ie�����
		objGo.addEventListener("onclick",function (){
			HideIframe(mDiv,objDiv);
		});*/
	
	}
	
	//textArea���
	function htmlEditorTextArea(id,ckeditor){
		//�б�ǩ
		var columnNameTag="textArea_colum";
		
	    var frm=document.getElementById("ifrm");
		var objDiv=document.getElementById("div1");
		var mDiv=document.getElementById("mDiv");
		mDiv.style.display='';
		frm.setAttribute("src", getRootPath()+"ckeditorplus/TextAreaServlet");
		objDiv.style.display = ""; //��ʾ������div
		
		var objGo=document.getElementById("btGo");
		objGo.onclick=function (){
			var controlName=frm.contentWindow.document.getElementById(columnNameTag).value;
			var htmlstr="<textarea  datafld=\"SYS_AREA\" type='text'  id=\""+controlName
			+"\" name=\""+controlName+"\" textarea_colum=\""+controlName+"\" ></textarea> ";
			ckeditor.insertHtml(htmlstr);
			HideIframe(mDiv,objDiv);
		};
		/*��ie�����
		objGo.addEventListener("onclick",function (){
			HideIframe(mDiv,objDiv);
		});*/
	}
	

	function HideIframe(mDiv,objDiv){
		mDiv.style.display='none';
		objDiv.style.display = "none"; //���ظ�����div
	}
	
	 function getRootPath(){ 
	     var pathName = window.location.pathname.substring(1); 
	     var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/')); 
	     return window.location.protocol + '//' + window.location.host + '/'+ webName + '/'; 
	} 
	    


	