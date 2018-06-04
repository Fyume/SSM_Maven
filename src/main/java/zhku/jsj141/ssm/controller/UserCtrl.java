package zhku.jsj141.ssm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import redis.clients.jedis.Jedis;
import zhku.jsj141.ssm.po.Temp;
import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;
import zhku.jsj141.ssm.utils.JedisUtils;
import zhku.jsj141.ssm.utils.MD5Utils;
import zhku.jsj141.ssm.utils.fileUtils;
import zhku.jsj141.ssm.utils.mailUtils;
import zhku.jsj141.ssm.utils.methodUtils;
import zhku.jsj141.ssm.validation.ValidGroup1;

@Controller
@RequestMapping("/user")
public class UserCtrl {
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private methodUtils mUtils;
	@Autowired
	private JedisUtils jUtils;
	/*@RequestMapping(value="/findUser",method={RequestMethod.POST,RequestMethod.GET})
	public String findUser(Model model) throws Exception{
		System.out.println("--findUser--");
		User user = userService.findUser("1");
		System.out.println(user);
		model.addAttribute("test", "model");
		return "test";
	}*/
	@RequestMapping(value="/login",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String login(User user,String cookieFlag){
		Temp t = new Temp();
		Map<String,Object> map2 = new HashMap<String,Object>();
		Map<String,String> map = new HashMap<String,String>();
		t.setResult("false");
		String UID = user.getUid();
		String MD5_p = new MD5Utils(user.getPassword()).getStr();
		try {
			User user_sql = userService.findUser(UID);
			if(user_sql!=null){
				if(user_sql.getPassword().equals(MD5_p)){
					map2.put("userinfo",user_sql);
					if(cookieFlag!=null){
						map.put("useCookie","true");
						String now = String.valueOf(System.currentTimeMillis());
						String token = new MD5Utils(user.getUid()).getStr()+now.substring(now.length()-4);
						Jedis jedis = jUtils.getJedis();
						jedis.setex(token,3600*24*7,user_sql.getUid());//加入新token
						map.put("ssm_m_user",token);//返回浏览器token
					}
					map2.put("cookies",map);
					t.setResult("true");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.setData(map2);
		return mUtils.OneResultO(t);
	}
	@RequestMapping(value="/logOut",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String logOut(){//清redis 没了
		Cookie cookies[] = request.getCookies();
		if(cookies!=null){//有 则取cookie
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("ssm_m_user")){
					if(cookie.getValue()!=""||cookie.getValue()!="null"){
						JedisUtils jUtils = new JedisUtils();
						Jedis jedis = jUtils.getJedis();
						String uid = jedis.get(cookie.getValue());//查找redis有没有这个token
						if(uid!=null){//有
							jedis.del(cookie.getValue());
						}
					}
				}
			}
		}
		return mUtils.OneResultM("true");
	}
	@RequestMapping(value="/ajaxUid",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxUid(){//用小型弹窗($Pro())
		String uid = request.getParameter("uid");
		if(uid!=null){
			try {
				if(userService.findUser(uid)!=null){
					return mUtils.OneResultM("该用户ID已存在Σ( ° △ °|||)︴");
				}else{
					return mUtils.OneResultM("用户ID可用φ(゜▽゜*)♪");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return mUtils.OneResultM("出错了ヽ(°◇° )ノ");
			}
		}
		return mUtils.OneResultM("用户ID为空o_O");
	}
	@RequestMapping(value="/register",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String register(User user){
		if(user.getEmail()!=null&&user.getEmail().trim()!=""){
			user.setEmail2(user.getEmail());//激活之后再存到email字段
			String time = String.valueOf(System.currentTimeMillis()/1000);
			time = time.substring(time.length()-4);
			String uid_MD5 = new MD5Utils(user.getUid()).getStr();
			try {
				mailUtils.sendEmail(user.getEmail(), (uid_MD5+time));
				user.setPassword(new MD5Utils(user.getPassword()).getStr());
				user.setEmail(null);
				user.setCode(uid_MD5+time);
				userService.insertSelective(user);
				return mUtils.OneResultM("注册成功，请前往邮箱激活");
			}catch (Exception e) {
				e.printStackTrace();
				return mUtils.OneResultM("false");
			}
		}
		return mUtils.OneResultM("false");
	}
	@RequestMapping(value="/activate",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String activate(){
		String code = request.getParameter("code");
		Temp temp = new Temp();
		temp.setTitle("激活结果");
		temp.setWhere("/SSM_Maven/user/linshi.do");
		if(code==null){
			temp.setResult("激活码为空，你是不是打算做什么不好的事啊@_@");
			return mUtils.OneResultO(temp);
		}
		User user = null;
		try {
			user = userService.findUserByCode(code);
			if(user!=null){
				String uid_MD5 = new MD5Utils(user.getUid()).getStr();
				if(uid_MD5.equals(code.substring(0,uid_MD5.length()))){//是同一个user，计算时间差
					long time = Long.valueOf(code.substring(uid_MD5.length(), code.length()));
					String now = String.valueOf(System.currentTimeMillis()/1000);
					now = now.substring(now.length()-4);
					long NOW = Long.valueOf(now);
					long time_temp = NOW-time;
					if(time_temp>0&&time_temp<600){//10分钟内
						user.setCode("null");
						user.setEmail(user.getEmail2());
						user.setEmail2("null");
						userService.updateSelective(user);
						temp.setResult("激活成功，现已开通会员功能\\(^o^)/~");
						return mUtils.OneResultO(temp);
					}else{
						temp.setResult("激活超时，请重新发送激活邮件哦(⊙ｏ⊙)~");
						return mUtils.OneResultO(temp);
					}
				}
			}else{
				temp.setResult("没有这个用户哦，你是不是搞错啦←_←");
				return mUtils.OneResultO(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			temp.setResult("报错了！报错了！o_O");
			return mUtils.OneResultO(temp);
		}
		temp.setResult("激活失败了o_O");
		return mUtils.OneResultO(temp);
	}
	@RequestMapping("/linshi")
	public String linshi(){
		
		return "index";
	}
	//@ModelAttribute(request域中的key)数据回显
	//@Validated和BindingResult
	@RequestMapping("/test")
	public String test(HttpServletRequest request,@Validated(value={ValidGroup1.class}) User user,BindingResult bindingResult,MultipartFile fileName) throws Exception{
		System.out.println("--test--");
		System.out.println(user);
		User user1 = userService.findUser(user.getUid());
		System.out.println(user1);
		if(bindingResult.hasErrors()){
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError objectError : allErrors) {
				//可以传到ajax中
				System.out.println(objectError);
			}
		}
		//上传图片处理
		fileUtils.moveImage(fileName);
		
		/*ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("test","333");
		modelAndView.setViewName("test");//配置好视图解析器的前后綴之后*/
		
		/*return "redirect:findUser.action";*/
		return "index2";
	}
}
