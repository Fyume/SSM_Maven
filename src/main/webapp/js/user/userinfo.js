$(document).ready(function(){
	var s = JSON.parse(localStorage.getItem("userinfo"));
	debugger
	$("#uid").html(s.uid);
	if(s.email=="null"){
		$("#email").html(s.email2+"(没激活)");
	}else{
		$("#email").html(s.email);
	}
	if(s.image!="null"){
		$("#image").src("/SSM_Maven"+s.image);
	}
});
function StorClear(){
	$.Pop('确定登出吗？','confirm',function(){
		$.ajax({
			url:"/SSM_Maven/user/logOut.do",
			type:'POST',
			dataType:'json',
			cache:false,
			success:function(data){
				$.cookie('ssm_m_user','');
				localStorage.setItem("userinfo",null);
				window.location.replace("/SSM_Maven/user/linshi.do");
			},
		});
	});
}