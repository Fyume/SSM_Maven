package zhku.jsj141.ssm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;
import zhku.jsj141.ssm.utils.MD5Utils;
import zhku.jsj141.ssm.utils.fileUtils;
import zhku.jsj141.ssm.utils.mailUtils;
import zhku.jsj141.ssm.validation.ValidGroup1;

@Controller
@RequestMapping("/user")
public class UserCtrl {
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	/*@RequestMapping(value="/findUser",method={RequestMethod.POST,RequestMethod.GET})
	public String findUser(Model model) throws Exception{
		System.out.println("--findUser--");
		User user = userService.findUser("1");
		System.out.println(user);
		model.addAttribute("test", "model");
		return "test";
	}*/
	@RequestMapping("/login")
	public String login(User user,String cookieFlag){
		if(cookieFlag==null){
			
		}
		System.out.println(user);
		return "index";
	}
	@RequestMapping("/register")
	@ResponseBody
	public String register(User user){
		Map<String, String> map = new HashMap<String, String>();
		if(user.getEmail()!=null&&user.getEmail().trim()!=""){
			user.setEmail2(user.getEmail());
			String time = String.valueOf(System.currentTimeMillis()/1000);
			time = time.substring(time.length()-4);
			String uid_MD5 = new MD5Utils(user.getUid()).getStr();
			try {
				mailUtils.sendEmail(user.getEmail(), (uid_MD5+time));
				user.setEmail(null);
				userService.insertSelective(user);
				map.put("result", "true");
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map.put("result", "false");
			}
		}
		String str = JSON.toJSONString(map);
		return str;
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
