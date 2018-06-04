var arr = [0,0];
function check($this){
	debugger
	var E = /[a-zA-Z0-9_]{4,}/;
	var id = $($this).attr("id");
	if(E.test($($this).val())){
		if(id=="uid"){
			arr[0]=1;
		}else if(id=="password"){
			arr[1]=1;
		}
	}else{
		if(id=="uid"){
			arr[0]=0;
		}else if(id=="password"){
			arr[1]=0;
		}
	}
	if(arr[0]+arr[1]==2){
		validateOn();
	}else{
		validateOff();
	}
}
function login(){
	var cookie = $("#cookieFlag").is(':checked');
	$.ajax({
		url:'/SSM_Maven/user/login.do',
		type:'POST',
		data:$("#LoginForm").serialize(),
		dataType:'json',
		async:false,
		cache:false,
		success:function(data){
			if(data.result!="false"){
				debugger
				if(data.data.cookies.useCookie=="true"){
					$.cookie('ssm_m_user',data.data.cookies.ssm_m_user,{expires:7,path:'/'});//将token存放到cookie
				}
				if (typeof(Storage) !== "undefined") {
					localStorage.setItem("userinfo", JSON.stringify(data.data.userinfo));
				} else {
					$.Pro('浏览器不支持 Web Storageヾ(。￣□￣)ﾂ゜゜゜');
				}
				$("#Right_ul").load("/SSM_Maven/html/index_user.html");
			}else{
				$.Pro('登录失败ヾ(ﾟ∀ﾟゞ)');
			}
		},
	});
}