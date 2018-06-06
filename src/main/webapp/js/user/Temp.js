$(document).ready(function(){
		var path = window.location.href;
		var que = path.indexOf("?");
		var equ = path.indexOf("=");
		if(path.substring(que+1)=='clr'){
			$.cookie('ssm_m_user','');
			localStorage.setItem("userinfo",null);
			window.location.replace("/SSM_Maven/user/linshi.do");
		}
		if(que!=-1&&equ!=-1){
			var VAR = path.substring(que+1,equ);
			var value = path.substring(equ+1,path.length);
			if(VAR=='code'){//激活
				$.ajax({
					url:"/SSM_Maven/user/activate.do",
					type:'POST',
					dataType:'json',
					cache:false,
					data:{code:value},
					success:function(data){
						$("#title").html(data.title);
						$("#content").html(data.result);
						locationTo(data.where);
					},
					error:function(){
						$("#title").html("返回结果");
						$("#content").html("额 你提交的请求好像被吃掉了o_O");
						locationTo("/SSM_Maven/user/linshi.do");
					}
				});
			}else{
				locationTo("/SSM_Maven/user/linshi.do");
			}
		}else{
			locationTo("/SSM_Maven/user/linshi.do");
		}
});
function locationTo(where){
	var NumDec = window.setInterval("numDec()", 1000);
	window.setInterval("window.clearInterval('NumDec');redirect('"+where+"')", 3000);
}
function numDec(){
	var num = $("#num").html();
	num = num - 1;
	$("#num").html(num);
}
function redirect(path){
	window.location.replace(path);
}