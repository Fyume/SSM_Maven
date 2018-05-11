package zhku.jsj141.ssm.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;
import zhku.jsj141.ssm.utils.fileUtils;
import zhku.jsj141.ssm.validation.ValidGroup1;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="/findUser",method={RequestMethod.POST,RequestMethod.GET})
	public String findUser(Model model) throws Exception{
		System.out.println("--findUser--");
		User user = userService.findUser("1");
		System.out.println(user);
		model.addAttribute("test", "model");
		return "test";
	}
	@RequestMapping("/login")
	public String login(){
		
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
