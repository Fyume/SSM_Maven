package zhku.jsj141.ssm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;

@Controller("webSocket")
@RequestMapping("/webSocket")
public class WebSocketCtrl implements WebSocketHandler{

    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("链接关闭......" + closeStatus.toString());
        //这里可以结合redis操作登录表
        users.remove(webSocketSession);
    }

	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("链接成功......");
        users.add(session);
        /*if(uid!= null){
            //查询未读消息
            int count = 5;
            session.sendMessage(new TextMessage(count + ""));
        }*/
        session.sendMessage(new TextMessage("欢迎使用聊天平台(测试中)~~"));
    }

	public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        String mess =  (String) webSocketMessage.getPayload();
        /*HttpServletRequest  req = ((ServletServerHttpRequest)request).getServletRequest();
        String uid = (String) req.getSession().getAttribute("uid");*/
        Map<String, Object> map = session.getAttributes();
        String jsonStr = (String) webSocketMessage.getPayload();
        JSONObject json = JSONObject.parseObject(jsonStr);
        if(json.get("uid")!=null){
        	 String uid = (String)json.get("uid");
        	 map.put("uid", uid);
        }else{
        	 String from = (String) json.get("from");//
             String to = (String) json.get("to");
             String message = (String) json.get("message");
             map.put("from", from);
             map.put("to", to);
             map.put("message", message);
             mess = "<span style=\"color:red;\">"+from+"</span>对你说: "+message;
             sendMessageToUser(to,new TextMessage(mess));
             mess = "你对<span style=\"color:red;\">"+from+"</span>说: "+message;
             sendMessageToUser(from,new TextMessage(mess));
        }
    }


	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()){
            webSocketSession.close();
        }
        System.out.println("链接出错，关闭链接......");
        users.remove(webSocketSession);
    }

	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String uid, TextMessage message) {
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("uid").equals(uid)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
