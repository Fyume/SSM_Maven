var li = $("#Right #Right_ul li");
var last = $("#Right #Right_ul li:last");
function disappear(str){
	$(str).css("display","none");
}
function appear(str){
	$(str).css("z-index",1);
}
function right_on($this){
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
//websocket
function getCon(){
	if (typeof WebSocket != 'undefined') {
		setMessageInnerHTML("连接中....");
		websocket = new WebSocket("ws://localhost:8080/SSM_Maven/webSocket.do");
	}else{
		alert('该浏览器不支持websocket,建议更换浏览器');
    }
	websocket.onmessage = function(event) {
		$("#returnMessage").html($("#returnMessage").html()+event.data +"<br/>");
	};
	websocket.onerror = function(){
		setMessageInnerHTML("连接失败");
    };
    websocket.onopen = function(event){
    	setMessageInnerHTML("连接创建成功....");
    	var json = {
    		uid : $("#uid").html()
    	}
    	websocket.send(JSON.stringify(json));
    };
	websocket.onclose = function(){
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
function closeWebSocket(){
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