$(document).ready(function() {
	$("#returnMessage").scrollTop($("#returnMessage").height());
	//聊天窗口
	$("#move").mousedown(function(e) {
		var offset = $("#chartBox").offset();
		$(document).bind('selectstart', function() {
			return false;
		});
		var x = e.pageX - offset.left;
		var y = e.pageY - offset.top;
		$(document).mousemove(function(e) {
			var x_ = e.pageX - x - 20;
			var y_ = e.pageY - y - 50;
			$("#chartBox").css("top", y_ + "px");
			$("#chartBox").css("left", x_ + "px");
			/*$("#chartBox").animate({left:x_+"px",top:y_+"px"},1);*/
		}).mouseup(function() {
			$(this).unbind("mousemove");
			$(this).selectstart(function() {
				return true;
			});
		});
	});
	getCon();
});
//websocket
function getCon() {
	var s = JSON.parse(window.localStorage.getItem("userinfo"));
	if (typeof WebSocket != 'undefined') {
		websocket = new WebSocket("ws://localhost:8080/SSM_Maven/webSocket.do");
	} else {
		$.Pop('该浏览器不支持websocket,建议更换浏览器');
	}
	websocket.onmessage = function(event) {
		setMessageInnerHTML(event.data);
	};
	websocket.onerror = function() {
		/*$("#btn_Con1").val("连接失败");*/
		$.Pro("连接失败", 1);
	};
	websocket.onopen = function(event) {
		$.Pro("连接成功", 1);
		/*    	$("#btn_Con1").val("连接成功");
		 $("#btn_Con1").attr("onclick","");
		 $("#btn_Con1").addClass("disabled");
		 */$("#btn_Con2").removeClass("disabled");
		var json = {
			uid : s.uid
		}
		websocket.send(JSON.stringify(json));
	};
	websocket.onclose = function() {
		/*$("#btn_Con1").val("建立连接");
		$("#btn_Con1").attr("onclick","getCon()");
		$("#btn_Con1").removeClass("disabled");*/
		$("#btn_Con2").addClass("disabled");
		$.Pro("连接已关闭");
	};
	//关闭窗口前先确保关闭了socket连接
	window.onbeforeunload = function() {
		websocket.close();
	};
}
function setTo($this) {
	var title = $($this).attr("title");
	$("#toID").val(title);
	$("#FriendList li").each(function(){
		if($(this).attr("title")==title){
			$(this).css("background","#d0ffff");
		}else{
			$(this).css("background","#ffffff");
		}
	});
}
//将消息显示在网页上 
function setMessageInnerHTML(innerHTML) {
	var json = JSON.parse(innerHTML);//没写完
	if(json.from!=null&&json.from!=''){
		$(YourBox(json.time,json.from,json.message)).appendTo($("#returnMessage"));
	}else{
		$(MyBox(json.time,json.message)).appendTo($("#returnMessage"));
	}
}
function YourBox(time,from,message){
	var colon = time.indexOf(":");
	var colon_last = time.lastIndexOf(":");
	var time_div = '';
	if(colon!=-1&&colon_last!=-1){
		var min = time.substring(colon+2,colon_last);
		if(min=='0'){
			time_div = '<div class="col-lg-12 text-center">—————————————————— <span>'+time+'</span> ——————————————————</div>';
		}
	}
	var div = '<div class="col-lg-12" style="padding-top:3px;">'+time_div+'<div class="col-lg-12" style="padding-top:15px;"><img style="margin-left:-10px;" class="ChartImg" title="'+from+'" src="/SSM_Maven/img/user/img-default.jpg"/> <span style="color:red;font-size:18px;">'+from+'</span>:</div><div class="pull-left ChartBox btlr-0"><span style="color:#000000;font-size:18px;">'+message+'</span></div></div>';
	return div;
}
function MyBox(time,message){
	var s = JSON.parse(window.localStorage.getItem("userinfo"));
	var colon = time.indexOf(":");
	var colon_last = time.lastIndexOf(":");
	var time_div = '';
	if(colon!=-1&&colon_last!=-1){
		var min = time.substring(colon+2,colon_last);
		if(min=='0'){
			time_div = '<div class="col-lg-12 text-center">—————————————————— <span>'+time+'</span> ——————————————————</div>';
		}
	}
	var div = '<div class="col-lg-12" style="padding-top:3px;">'+time_div+'<div class="col-lg-12 text-right" style="padding-top:15px;"><img class="ChartImg" title="'+s.uid+'" src="/SSM_Maven/img/user/img-default.jpg"/></div><div class="pull-right text-right ChartBox btrr-0" style="margin-right: 50px;"><span style="color:#000000;font-size:18px;">'+message+'</span></div></div>';
	return div;
}
function closeCon() {
	websocket.close();
}
//发送消息
function send() {
	var s = JSON.parse(window.localStorage.getItem("userinfo"));
	//接收者名称
	var to = $("#toID").val();
	if ('' == to) {
		alert("请填写接收者");
		return false;
	}
	//发送的消息
	var message = $("#message").val();
	if ('' == message) {
		alert("请填写发送信息");
		return false;
	}
	var json = {
		from : s.uid,
		to : to,
		message : message
	}
	var str = JSON.stringify(json)
	websocket.send(str);
	$("#message").val("");
}