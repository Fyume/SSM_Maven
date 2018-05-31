package zhku.jsj141.ssm.controller;

import java.util.List;

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

import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;
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
	@RequestMapping(value="/ajaxUid",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxUid(){//用小型弹窗($Pro())
		String uid = request.getParameter("uid");
		if(uid!=null){
			try {
				if(userService.findUser(uid)!=null){
					return mUtils.OneResultM("该用户ID已存在");
				}else{
					return mUtils.OneResultM("用户ID可用");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mUtils.OneResultM("false");
	}
	@RequestMapping("/register")
	@ResponseBody
	public String register(User user){
		if(user.getEmail()!=null&&user.getEmail().trim()!=""){
			user.setEmail2(user.getEmail());//激活之后再存到email字段
			String time = String.valueOf(System.currentTimeMillis()/1000);
			time = time.substring(time.length()-4);
			String uid_MD5 = new MD5Utils(user.getUid()).getStr();
			try {
				mailUtils.sendEmail(user.getEmail(), (uid_MD5+time));
				user.setEmail(null);
				userService.insertSelective(user);
				return mUtils.OneResultM("注册成功，请前往邮箱激活");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mUtils.OneResultM("false");
	}
	@RequestMapping("/activate")
	@ResponseBody
	public String activate(User user){
		if(user.getEmail()!=null&&user.getEmail().trim()!=""){
			user.setEmail2(user.getEmail());
			String time = String.valueOf(System.currentTimeMillis()/1000);
			time = time.substring(time.length()-4);
			String uid_MD5 = new MD5Utils(user.getUid()).getStr();
			try {
				mailUtils.sendEmail(user.getEmail(), (uid_MD5+time));
				user.setEmail(null);
				userService.insertSelective(user);
				return mUtils.OneResultM("注册成功，请前往邮箱激活");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mUtils.OneResultM("false");
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
