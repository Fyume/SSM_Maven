var li = $("#Right #Right_ul li");
var last = $("#Right #Right_ul li:last");
function right_on($this){
	$($this).attr("onclick","right_off(this)");
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
/*function disapear(){
	last.css("display","none");
	last = last.prev();
	if(last.html()==undefined){
		last = $("#Right #Right_ul li:last");
	}
}*/
function right_off($this){
	$($this).attr("onclick","right_on(this)");
	var rightOff = window.setInterval("liMove(1)",100);
	var rightOnOff = window.setInterval("window.clearInterval("+rightOff+")",100*(li.length));
	/*var d = window.setInterval("disapear()",100);
	window.setInterval("window.clearInterval("+d+")",100*(li.length));*/
}
