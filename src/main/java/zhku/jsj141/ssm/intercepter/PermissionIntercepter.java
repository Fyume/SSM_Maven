package zhku.jsj141.ssm.intercepter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import zhku.jsj141.ssm.utils.JedisUtils;

public class PermissionIntercepter implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		Cookie cookies[] = request.getCookies();
		boolean rs = false;
		if(cookies!=null){//有 则取cookie
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("ssm_m_user")){
					if(cookie.getValue()!=""||cookie.getValue()!="null"){
						rs = true;
						JedisUtils jUtils = new JedisUtils();
						Jedis jedis = jUtils.getJedis();
						String uid = jedis.get(cookie.getValue());//查找redis有没有这个token
						if(uid!=null){//有
							//放行
							return true;
						}else{//无 通知前端清除cookie
							response.sendRedirect(request.getContextPath() + "/html/Temp.html?clr"); 
						}
					}
				}
			}
		}
		if(!rs){//无cookie
			response.sendRedirect(request.getContextPath() + "/html/Temp.html?clr"); 
		}
		return false;
	}
}
