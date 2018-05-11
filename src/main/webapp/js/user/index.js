var arr = $("#Left #Left_ul li");
var temp = $("#Left #Left_ul li:last");
function left_on($this){
	$($this).attr("onclick","left_off(this)");
	var leftOn = window.setInterval("liMove(0)",300);
	var leftOnOff = window.setInterval("window.clearInterval("+leftOn+")",300*(arr.length));
}
function liMove(num){
	if(num==0){
		$.each(arr,function(){
			if($(this).css("display")=="none"){
				$(this).css("animation","liMove 1s");
				$(this).css("animation-fill-mode","forwards");
				$(this).css("display","block");
				return false;
			}
		});
	}else{
		temp.css("animation","liLeave 1s");
		temp.css("animation-fill-mode","backwards");
		temp.css("display","none");
		temp = temp.prev();
		if(temp.html()==undefined){
			temp = $("#Left #Left_ul li:last");
		}
	}
}
function left_off($this){
	$($this).attr("onclick","left_on(this)");
	var leftOff = window.setInterval("liMove(1)",100);
	var leftOnOff = window.setInterval("window.clearInterval("+leftOff+")",100*(arr.length));
}
