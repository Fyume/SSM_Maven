var li = $("#Right #Right_ul li");
var last = $("#Right #Right_ul li:last");

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
			var x_ = e.pageX - x - 300;
			var y_ = e.pageY - y - 100;
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
	var s = JSON.parse(localStorage.getItem("userinfo"));
	debugger
	if(s!=null){
		$("#Right_ul").load("/SSM_Maven/html/index_user.html");
	}else{
		$("#Right_ul").load("/SSM_Maven/html/login.html");
	}
});

/*function userInfo(){
	
	$("#uid").html(uid);
	if(email=="null"){
		$("#email").html(email2+"(没激活)");
	}else{
		$("#email").html(email);
	}
}*/

function Reset(){
	li = $("#Right #Right_ul li");
	last = $("#Right #Right_ul li:last");
}
function disappear(str){
	$(str).css("display","none");
}
function appear(str){
	$(str).css("z-index",1);
}
function right_on($this){
	Reset();
	$($this).attr("onclick","right_off(this)");
	appear("#Right");
	var rightOn = window.setInterval("liMove(0)",300);
	var rightOnOff = window.setInterval("window.clearInterval("+rightOn+")",300*(li.length));
}
function liMove(num){
	if(num==0){
		$.each(li,function(){
			if($(this).css("display")=="none"){
				$(this).css("animation","liMove 1s");
				$(this).css("display","block");
				return false;
			}
		});
	}else{
		last.css("animation","liLeave 1s");
		last.css("display","none");
		last = last.prev();
		if(last.html()==undefined){
			last = $("#Right #Right_ul li:last");
		}
	}
}
function right_off($this){
	Reset();
	$($this).attr("onclick","right_on(this)");
	$("#Right").css("z-index","-1");
	var rightOff = window.setInterval("liMove(1)",100);
	var rightOnOff = window.setInterval("window.clearInterval("+rightOff+");",100*(li.length));
	/*var d = window.setInterval("disapear()",100);
	window.setInterval("window.clearInterval("+d+")",100*(li.length));*/
}
/*function disapear(){
last.css("display","none");
last = last.prev();
if(last.html()==undefined){
	last = $("#Right #Right_ul li:last");
}
}*/
function TopClose(){
	right_off($("#right_btn"));
	$("#Top").css("animation","topClose 1s");
	var topOff = window.setInterval("closeAll()",1000);
	window.setInterval("window.clearInterval("+topOff+")",1000);
}

function closeAll(){
	disappear("#Top");
	$("#topOn").css("display","block");
}
function TopOpen($this){
	$("#Top").css("animation","topOpen 1s");
	$("#Top").css("display","block");
	$($this).css("display","none");
}
/***注册***/
function registerOn(){
	$("#Right_ul").load("/SSM_Maven/html/register.html");
}
/*****登录*****/
function loginOn(){
	$("#Right_ul").load("/SSM_Maven/html/login.html");
}

//websocket
function getCon(){
	if (typeof WebSocket != 'undefined') {
		$("#btn_Con1").val("连接中...");
		websocket = new WebSocket("ws://localhost:8080/SSM_Maven/webSocket.do");
	}else{
		alert('该浏览器不支持websocket,建议更换浏览器');
    }
	websocket.onmessage = function(event) {
		$("#returnMessage").html($("#returnMessage").html()+event.data +"<br/>");
	};
	websocket.onerror = function(){
		$("#btn_Con1").val("连接失败");
    };
    websocket.onopen = function(event){
    	$("#btn_Con1").val("连接成功");
    	$("#btn_Con1").attr("onclick","");
    	$("#btn_Con1").addClass("disabled");
    	$("#btn_Con2").removeClass("disabled");
    	var json = {
    		uid : $("#uid").html()
    	}
    	websocket.send(JSON.stringify(json));
    };
	websocket.onclose = function(){
		$("#btn_Con1").val("建立连接");
		$("#btn_Con1").attr("onclick","getCon()");
		$("#btn_Con2").addClass("disabled");
    	$("#btn_Con1").removeClass("disabled");
    	setMessageInnerHTML("连接已关闭");
   };
   //关闭窗口前先确保关闭了socket连接
	window.onbeforeunload = function(){
		websocket.close();
   };
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
    		from:$("#uid").html(),
    		to:to,
    		message:message
    }
    var str = JSON.stringify(json)
    websocket.send(str);
}