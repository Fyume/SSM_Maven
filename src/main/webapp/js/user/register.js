var uidRs = false;
var passRs = false;
var emailRs = false;
function visibility(){
	var type = $("#password").attr("type");
	if(type=='password'){
		$("#visibility").removeClass("glyphicon-eye-close");
		$("#visibility").addClass("glyphicon-eye-open");
		$("#password").attr("type","text");
	}else if(type=='text'){
		$("#visibility").removeClass("glyphicon-eye-open");
		$("#visibility").addClass("glyphicon-eye-close");
		$("#password").attr("type","password");
	}
}
function ajaxUid($this){
	var UID = /[a-zA-Z0-9_]{4,15}/;
	if(UID.test($("#uid").val())){
		$.ajax({
			url:'/SSM_Maven/user/ajaxUid.do',
			type:'POST',
			data:{uid:$("#uid").val()},
			dataType:'JSON',
			async:false,
			cache:false,
			success:function(data){
				$.Pro(data.result);
				if(data.result=="用户ID可用φ(゜▽゜*)♪"){
					uidRs=true;
					checkRG();
				}else{
					uidRs=false;
					$($this).focus();
					checkRG();
				}
			},
		});
	}else{
		$.Pro("这个ID好像有点不对劲ヽ(°◇° )ノ");
	}
}
function password_RG(){
	var pass = $("#password").val();
	var Pass = /[a-zA-Z0-9_]{6,10}/;//6-10位
	debugger
	if(Pass.test(pass)==true){
		passRs=true;
		checkRG();
	}else{
		$.Pro("密码由6-10位数字字符下划线组成");
		passRs=false;
		checkRG();
	}
}
function email_RG(){
	var email = $("#email").val();
	var Email = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;//网上找的
	debugger
	if(Email.test(email)==true){
		emailRs=true;
		checkRG();
	}else{
		$.Pro("邮箱格式好像有点不对哦→_→");
		emailRs=false;
		checkRG();
	}
}
function checkRG(){
	if(uidRs==true&&passRs==true&&emailRs==true){
		$("#RegistBtn").removeClass("disabled");
	}else{
		$("#RegistBtn").addClass("disabled");
	}
}
function register(){
	$("#RegistBtn").addClass("disabled");
	if(passRs==true&&uidRs==true&&emailRs==true){
		$.ajax({
			url:'/SSM_Maven/user/register.do',
			type:'POST',
			data:$("#RegisterForm").serialize(),
			dataType:'JSON',
			cache:false,
			success:function(data){
				if(data.result!="false"){
					$.Pop(data.result);
				}else{
					$.Pop('注册失败');
				}
			},
		});
	}
}