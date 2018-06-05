$(document).ready(function(){
	$("#returnMessage").scrollTop($("#returnMessage").height());
	//聊天窗口
	$("#move").mousedown(function(e){
		var offset = $("#chartBox").offset();
		$(document).bind('selectstart',function(){
			return false;
		});
		var x = e.pageX - offset.left;
		var y = e.pageY - offset.top;
		$(document).mousemove(function(e){
			var x_ = e.pageX - x - 20;
			var y_ = e.pageY - y - 50;
			$("#chartBox").css("top",y_+"px");
			$("#chartBox").css("left",x_+"px");
			/*$("#chartBox").animate({left:x_+"px",top:y_+"px"},1);*/
		}).mouseup(function(){  
	        $(this).unbind("mousemove");
	        $(this).selectstart(function(){
				return true;
			});
	    });
	});
	getCon();
});
//websocket
function getCon(){
	var s = JSON.parse(window.localStorage.getItem("userinfo"));
	if (typeof WebSocket != 'undefined') {
		websocket = new WebSocket("ws://localhost:8080/SSM_Maven/webSocket.do");
	}else{
		$.Pop('该浏览器不支持websocket,建议更换浏览器');
    }
	websocket.onmessage = function(event) {
		$("#returnMessage").html($("#returnMessage").html()+event.data +"<br/>");
	};
	websocket.onerror = function(){
		/*$("#btn_Con1").val("连接失败");*/
		$.Pro("连接失败",1);
    };
    websocket.onopen = function(event){
    	$.Pro("连接成功",1);
/*    	$("#btn_Con1").val("连接成功");
    	$("#btn_Con1").attr("onclick","");
    	$("#btn_Con1").addClass("disabled");
*/    	$("#btn_Con2").removeClass("disabled");
		debugger
    	var json = {
    		uid : s.uid
    	}
    	websocket.send(JSON.stringify(json));
    };
	websocket.onclose = function(){
		/*$("#btn_Con1").val("建立连接");
		$("#btn_Con1").attr("onclick","getCon()");
		$("#btn_Con1").removeClass("disabled");*/
		$("#btn_Con2").addClass("disabled");
    	$.Pro("连接已关闭");
   };
   //关闭窗口前先确保关闭了socket连接
	window.onbeforeunload = function(){
		websocket.close();
   };
}
function setTo($this){
	$("#toID").val($($this).attr("title"));
}
//将消息显示在网页上 
function setMessageInnerHTML(innerHTML){
    $("#returnMessage").html($("#returnMessage").html()+innerHTML +"<br/>");
}
function closeCon(){
    websocket.close();
}
//发送消息
function send(){
	var s = JSON.parse(window.localStorage.getItem("userinfo"));
    //接收者名称
    var to = $("#toID").val();
    if('' == to){
        alert("请填写接收者");
        return false;
    }
    //发送的消息
    var message = $("#message").val();
    if('' == message){
        alert("请填写发送信息");
        return false;
    }
    var json = {
    		from:s.uid,
    		to:to,
    		message:message
    }
    var str = JSON.stringify(json)
    websocket.send(str);
}