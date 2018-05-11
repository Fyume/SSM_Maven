package zhku.jsj141.ssm.Exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class testExceptionResolver implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception e) {
		//若是 自定义异常(testException)
		/*String message = null;
		if(e instanceof testException){
			message = ((testException)e).getMessage();
		}else{
			message="出现未知错误！";
		}
		*/
		//将上面代码改成
		testException te = null;
		if(e instanceof testException){
			te = (testException)e;
		}else{
			te = new testException("出现未知错误！");
		}
		String message = te.getMessage();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", message);
		modelAndView.setViewName("error");
		return modelAndView;
	}

}
