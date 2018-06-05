$(document).ready(function(){
	var s = JSON.parse(localStorage.getItem("userinfo"));
	$("#uid").html(s.uid);
	$("#uid").parent().attr("title",s.uid);
	if(s.email=="null"){
		$("#email").html(s.email2+"(没激活)");
		$("#email").parent().attr("title",s.email2);
	}else{
		$("#email").html(s.email);
		$("#email").parent().attr("title",s.email);
	}
	if(s.image!="null"&&s.image!=null){
		$("#image").attr("src","/SSM_Maven"+s.image);
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
function MyF(){
	$("#Bottom").load("/SSM_Maven/html/MyFriend.html");
}