package zhku.jsj141.ssm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="/findUser",method={RequestMethod.POST,RequestMethod.GET})
	public String findUser(Model model) throws Exception{
		System.out.println("--findUser--");
		User user = userService.findUser(1);
		System.out.println(user);
		model.addAttribute("test", "model");
		return "test";
	}
	@RequestMapping("/test")
	public String test(HttpServletRequest request, User user) throws Exception{
		System.out.println("--test--");
		System.out.println(user);
		/*ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("test","333");
		modelAndView.setViewName("test");//配置好视图解析器的前后綴之后*/
		/*return "redirect:findUser.action";*/
		return "test";
	}
}
