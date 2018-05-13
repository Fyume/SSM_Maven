package zhku.jsj141.ssm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import zhku.jsj141.ssm.controller.WebSocketCtrl;
import zhku.jsj141.ssm.intercepter.MyHandshakeInterceptor;
@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	/*public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketCtrl(),"/echo").addInterceptors(new HandshakeInterceptor()); //支持websocket 的访问链接
        registry.addHandler(new WebSocketCtrl(),"/sockjs/echo").addInterceptors(new HandshakeInterceptor()).withSockJS(); //不支持websocket的访问链接
    }*/
	@Autowired
	private WebSocketCtrl myHander;
	
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 添加处理器
    	//.setAllowedOrigins("*")这个很关键。。。。(跨域请求)
        registry.addHandler(myHander, "/webSocket.do").addInterceptors(new MyHandshakeInterceptor()).setAllowedOrigins("*");
    }

    /*@Bean
    public WebSocketCtrl myHandler() {
        return new WebSocketCtrl();
    }*/
}
