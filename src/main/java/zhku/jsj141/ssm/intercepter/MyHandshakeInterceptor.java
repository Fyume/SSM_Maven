package zhku.jsj141.ssm.intercepter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class MyHandshakeInterceptor implements HandshakeInterceptor{

	public void afterHandshake(ServerHttpRequest request,  
            ServerHttpResponse response, WebSocketHandler wsHandler,  
            Exception ex) {
		 System.out.println("After Handshake");  
	}

	public boolean beforeHandshake(ServerHttpRequest request,  
            ServerHttpResponse response, WebSocketHandler wsHandler,  
            Map<String, Object> attributes) throws Exception {
		HttpServletRequest  req = ((ServletServerHttpRequest)request).getServletRequest();
		String uid = req.getParameter("uid");
		req.getSession().setAttribute("uid", uid);
		System.out.println("Before Handshake");  
		return true;  
	}
	  
}  
