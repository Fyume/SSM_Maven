function validateOn(){
	$(document).ready(function() {
		$("#btn_validate").mousedown(function(e) {// 判断按键
			$(document).bind({selectstart:function(){
				return false;
			}});
			var slideBox = $("#slide_box");//底部div
			var slideXbox = $("#slide_xbox");//绿色div
			var slideBoxWidth = slideBox.outerWidth();
			var btnWidth = $(this).outerWidth();//小方块宽度
			var start = e.clientX;
			var btnleft = $(this).offset().left;//最初小方块距离左边的距离
			var disX =  start - btnleft;//鼠标距离小方块左边的距离
			document.onmousemove = function(e) {
				var left = e.clientX - disX;//小方块当前距离左边的距离
				var objX =  e.clientX - start + btnWidth;//小方块最右边离屏幕左边的距离（也就是绿色div要设置的长度）
				if(left<btnleft){
					left = btnleft;
				}else if(left>(btnleft+slideBoxWidth)){
					left = btnleft+slideBoxWidth;
				}
				if (objX < btnWidth) {
					objX = btnWidth;
				}
				if (objX > slideBoxWidth) {
					objX = slideBoxWidth;
				}
				$("#btn_validate").css("left",left+"px");
				$('#slide_xbox').css("width",objX + 'px');
			};
			document.onmouseup = function(e) {
				var objX =  e.clientX - start + btnWidth;
				if (objX <= slideBoxWidth) {
					objX = btnWidth;
					$("#btn_validate").css("left",(start-disX)+"px");//复位
				}else{
					objX = slideBoxWidth;
					$("#btn_validate").addClass("disabled");
					$("#btn_validate").attr("disabled","disabled");
					$("#btn_login").removeClass("disabled");
					$("#btn_login").removeAttr("disabled");
					$('#slide_xbox').html('验证通过<div id="btn_validate"><span class="glyphicon glyphicon-forward"></span></div>');
					$("#uid").attr("readonly","readonly");
					$("#uid").css("background-color","#c0c0c0");
					$("#password").attr("readonly","readonly");
					$("#password").css("background-color","#c0c0c0");
				}
				$('#slide_xbox').css("width",objX + 'px');
				document.onmousemove = null;
				document.onmouseup = null;	
			};
		}).mouseup(function(){
			$(document).bind({selectstart:function(){
				return true;
			}});
		});
		// 禁止审查元素
		$(document).bind("contextmenu", function(e) {
			return false;
		});
	});
}
function validateOff(){
	$(document).ready(function() {
		$("#btn_validate").mousedown(function(){return false;});
	});
}