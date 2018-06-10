var li = $("#Right #Right_ul li");
var last = $("#Right #Right_ul li:last");

$(document).ready(function(){
	var s = JSON.parse(localStorage.getItem("userinfo"));
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
	$("#indexMenu").removeClass("indexMenu");
	$("#indexMenu").addClass("indexMenu2");
	$("#indexMenu").next().css("height","20px");
	$("#indexMenuBtn").removeClass("hidden");
}
function TopOpen($this){
	if(!Menu){
		indexMenuOpen($("#indexMenuBtn"));
	}
	$("#Top").css("animation","topOpen 1s");
	$("#Top").css("display","block");
	$($this).css("display","none");
	$("#indexMenu").removeClass("indexMenu2");
	$("#indexMenu").addClass("indexMenu");
	$("#indexMenu").css("animation","");
	$("#indexMenu").next().css("height","100px");
	$("#indexMenuBtn").addClass("hidden");
}
function indexMenuScroll($this){
	var id = $($this).attr("title");
	var top = $("#"+id).offset().top;
	$(document).scrollTop(top-100);
}
var Menu = true;
function indexMenuCompress($this,animation){
	Menu = false;
	var j=false;
	if(animation==undefined){
		animation = "Menu-li 0.5s";
	}
	$("#indexMenuUl li").each(function(){
		$(this).addClass("borderRadius");
		if(!j){
			$("#indexMenu").css("animation","Menu 0.5s");
			$("#indexMenu").css("animation-fill-mode","forwards");j=true;
		}else{
			$(this).css("animation",animation);
			$(this).css("animation-fill-mode","forwards");
		}
	});
	$($this).children("span:first-child").appendTo($($this));
	$($this).attr("onclick","indexMenuOpen(this)");
	$("#indexMenuBtn2").removeClass("hidden");
	$("#indexMenuBtn3").removeClass("hidden");
}
function indexMenuOpen($this){
	Menu = true;
	var j=false;
	var n = $("#indexMenuUl li:last-child").attr("value");//最后一个元素的序号
	for(var i = 0;i<9-n;i++){
		MenuComDown();
	}
	$("#indexMenuUl").css("z-index",1);
	$("#indexMenuUl li").each(function(){
		$(this).removeClass("borderRadius");
		if(!j){
			$("#indexMenu").css("animation","Menu-res 0.5s");
			$("#indexMenu").css("animation-fill-mode","forwards");j=true;
		}else{
			$(this).css("animation","Menu-li-res 0.5s");
			$(this).css("animation-fill-mode","forwards");
		}
	});
	$($this).children("span:first-child").appendTo($($this));
	$($this).attr("onclick","indexMenuCompress(this)");
	$("#indexMenuBtn2").addClass("hidden");
	$("#indexMenuBtn3").addClass("hidden");
}
function MenuComUp(){
	$("#indexMenuUl li:last-child").css("animation","");
	$("#indexMenuUl").prepend($("#indexMenuUl li:last-child"));
	indexMenuCompress(undefined,"Menu-li-2 0.5s");
}
function MenuComDown(){
	$("#indexMenuUl li:first-child").next().css("animation","");
	$("#indexMenuUl").append($("#indexMenuUl li:first-child"));
	indexMenuCompress();
}
/***注册***/
function registerOn(){
	$("#Right_ul").load("/SSM_Maven/html/register.html");
}
/*****登录*****/
function loginOn(){
	$("#Right_ul").load("/SSM_Maven/html/login.html");
}