var num = 0;
function fileQueueError(file, errorCode, message) {
	try {
		var errorName = "文件上传错误!";
		if (errorCode == SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			errorName = "只能选择一个批次导入.";
		}
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			errorName = "文件大小为0!";
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			errorName = "文件大小超过限制!";
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
		default:			
			break;
		}
		$("#log").hide();
		$("#log").html('');
		uploadFileInfo(errorName);
		
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);
		
		
	} catch (ex) {
		this.debug(ex);
	}
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	num = numFilesQueued;
	
	startUploadFile();
}

function fileQueued(file){
	saveFileId(file.id);
	//addReadyFileInfo(file.id,file.name,file.size,"成功加载到队列");
	var listitem = '<li id="' + file.id + '" >' +
	'文件名: <em>' + file.name + '</em> (' + Math.round(file.size / 1024) + ' KB) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="progressvalue" ></span>' +
	'<div class="progressbar" ><div class="progress" ></div></div>' +
	'<p class="status" >文件上传中....</p>' +
	'</li>';
	$('#log').append(listitem);
		if(num == 1 ){
			$("#log").OpenDiv();
		}	
		for (i = 0; i <= file.index; i++){
			num ++;
		}
	$('li#' + file.id + ' .stopUpload').bind('click', function () { //Remove from queue on cancel click
		$("#"+file.id).remove();
		$("#log").CloseDiv();
	});
}

function uploadProgress(file, bytesLoaded) {
//	if($('.success').find('span.progressvalue').text() != '0%'){
//		
//	}
	var percentage = Math.round((bytesLoaded / file.size) * 100);
	if (percentage>100){
		percentage = 100;
	}
    $('#log li#' + file.id).find('div.progress').css('width', percentage + '%');
    $('#log li#' + file.id).find('span.progressvalue').text(percentage + '%');
}

function uploadError(file, errorCode, message) {
	var imageName =  "文件上传出错!";
	try {
		$("#log").CloseDiv();
		$("#log").html('');
		switch (errorCode) {		
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			imageName = "文件大小超过限制!";
			break;
		default:			
			break;
		}
		uploadFileInfo(imageName);
	} catch (ex3) {
		this.debug(ex3);
	}

}

function uploadSuccess(file, serverData) {
	//上传成功
	//refreshElecRecordGrid();
	//refreshElecMaterialGrid();
	 var item = $('#log li#' + file.id);
     item.find('div.progress').css('width', '100%');
     item.find('span.progressvalue').text('100%');
     item.addClass('success').find('p.status').html('上传完成!!! ');
     $('#log li#' + file.id).find('span.stopUpload').css('background-image','none') ;
     $('#log li#' + file.id).find('span.stopUpload').css('cursor','default') ;
   	 $("#"+file.id).remove();
   	 
}

function uploadComplete(file) {
	try {
		num = 0;
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued > 0) {
			this.startUpload();
			
		} else {
			for (i = 0; i <= file.index; i++){
				num = 0;
				$("#log").CloseDiv();	
				//$("#windownbg").remove();
				//$("#windown-box").fadeOut("slow", function () { $(this).remove(); });
			}
			originUploadComplete();//上传成功	
		}
	} catch (ex) {
		num = 0;
		$("#log").CloseDiv();
		this.debug(ex);
	}
}


function cancelUpload(){
	var infoTable = document.getElementById("infoTable");
	var rows = infoTable.rows;
	var ids = new Array();
	var row;
	if(rows==null){
		return false;
	}/*else{
		while(rows.length>1){
			infoTable.deleteRow(1);
		}
	}*/
	for(var i=1;i<rows.length;i){
		deleteFile(rows[i].id);
	}	
}

function deleteFile(fileId){
	//用表格显示
	var infoTable = document.getElementById("infoTable");
	var row = document.getElementById(fileId);
	infoTable.deleteRow(row.rowIndex);
	swfu.cancelUpload(fileId,false);
}


function addImage(src) {
	var newImg = document.createElement("img");
	newImg.style.margin = "5px";

	document.getElementById("thumbnails").appendChild(newImg);
	if (newImg.filters) {
		try {
			newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
		} catch (e) {
			// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
			newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
		}
	} else {
		newImg.style.opacity = 0;
	}

	newImg.onload = function () {
		fadeIn(newImg, 0);
	};
	newImg.src = src;
}

function fadeIn(element, opacity) {
	var reduceOpacityBy = 5;
	var rate = 30;	// 15 fps


	if (opacity < 100) {
		opacity += reduceOpacityBy;
		if (opacity > 100) {
			opacity = 100;
		}

		if (element.filters) {
			try {
				element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
			}
		} else {
			element.style.opacity = opacity / 100;
		}
	}

	if (opacity < 100) {
		setTimeout(function () {
			fadeIn(element, opacity);
		}, rate);
	}
}



/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
	this.fileProgressID = "divFileProgress";
	
	this.fileProgressWrapper = document.getElementById(this.fileProgressID);
	if (!this.fileProgressWrapper) {
		this.fileProgressWrapper = document.createElement("div");
		this.fileProgressWrapper.className = "progressWrapper";
		this.fileProgressWrapper.id = this.fileProgressID;

		this.fileProgressElement = document.createElement("div");
		this.fileProgressElement.className = "progressContainer";

		var progressCancel = document.createElement("a");
		progressCancel.className = "progressCancel";
		progressCancel.href = "#";
		progressCancel.style.visibility = "hidden";
		progressCancel.appendChild(document.createTextNode(" "));

		var progressText = document.createElement("div");
		progressText.className = "progressName";
		progressText.appendChild(document.createTextNode("上传文件: "+file.name));

		var progressBar = document.createElement("div");
		progressBar.className = "progressBarInProgress";

		var progressStatus = document.createElement("div");
		progressStatus.className = "progressBarStatus";
		progressStatus.innerHTML = "&nbsp;";

		this.fileProgressElement.appendChild(progressCancel);
		this.fileProgressElement.appendChild(progressText);
		this.fileProgressElement.appendChild(progressStatus);
		this.fileProgressElement.appendChild(progressBar);

		this.fileProgressWrapper.appendChild(this.fileProgressElement);
		document.getElementById(targetID).style.height = "75px";
		document.getElementById(targetID).appendChild(this.fileProgressWrapper);
		fadeIn(this.fileProgressWrapper, 0);

	} else {
		this.fileProgressElement = this.fileProgressWrapper.firstChild;
		this.fileProgressElement.childNodes[1].firstChild.nodeValue = "上传文件: "+file.name;
	}

	this.height = this.fileProgressWrapper.offsetHeight;

}
FileProgress.prototype.setProgress = function (percentage) {
	this.fileProgressElement.className = "progressContainer green";
	this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
	this.fileProgressElement.childNodes[3].style.width = percentage + "%";
};
FileProgress.prototype.setComplete = function () {
	this.fileProgressElement.className = "progressContainer blue";
	this.fileProgressElement.childNodes[3].className = "progressBarComplete";
	this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setError = function () {
	this.fileProgressElement.className = "progressContainer red";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setCancelled = function () {
	this.fileProgressElement.className = "progressContainer";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setStatus = function (status) {
	this.fileProgressElement.childNodes[2].innerHTML = status;
};

FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
	this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
	if (swfuploadInstance) {
		var fileID = this.fileProgressID;
		this.fileProgressElement.childNodes[0].onclick = function () {
			swfuploadInstance.cancelUpload(fileID);
			return false;
		};
	}
};
