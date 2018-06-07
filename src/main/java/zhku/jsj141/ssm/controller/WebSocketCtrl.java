package zhku.jsj141.ssm.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import zhku.jsj141.ssm.po.Chart;
import zhku.jsj141.ssm.service.ChartService;
import zhku.jsj141.ssm.service.UserService;
import zhku.jsj141.ssm.utils.JedisUtils;
import zhku.jsj141.ssm.utils.MD5Utils;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;

@Controller("webSocket")
@RequestMapping("/webSocket")
public class WebSocketCtrl implements WebSocketHandler{
	private static final JedisUtils JUtils = new JedisUtils();
	private static final Jedis jedis = JUtils.getJedis();
    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
    @Autowired
    private UserService userService;
    @Autowired
    private ChartService chartService;
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接关闭......" + closeStatus.toString());
        //这里可以结合redis操作连接记录表
        Map<String, Object> map = session.getAttributes();
        String uid = (String) map.get("uid");
        jedis.hdel("ssm_m_socket", uid);
        users.remove(session);
    }

	public void afterConnectionEstablished(WebSocketSession session) throws IOException {
		System.out.println("连接成功......");
		users.add(session);
       /* session.sendMessage(new TextMessage("欢迎使用聊天平台(测试中)~~"));*/
    }

	public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        Map<String, Object> map = session.getAttributes();
        Map<String, String> messMap = new HashMap<String, String>();
        String jsonStr = (String) webSocketMessage.getPayload();
        JSONObject json = JSONObject.parseObject(jsonStr);
        if(json.get("uid")!=null){
        	String uid = (String)json.get("uid");
        	if(userService.findUser(uid)!=null){
        		map.put("uid", uid);
        		Map<String, String> loginMap = jedis.hgetAll("ssm_m_socket");
    			loginMap.put(uid, String.valueOf(System.currentTimeMillis()));
    			jedis.hmset("ssm_m_socket", loginMap);
        	}
        }else{
        	String from = (String) json.get("from");
            String to = (String) json.get("to");
            if(!from.equals(to)){
            	String message = (String) json.get("message");
                long time = System.currentTimeMillis();
            	Chart chart = new Chart(from, to, message, time);
        		chartService.insert(chart);
        		//记得换成json格式
        		messMap.put("from", from);
        		messMap.put("message", message);
        		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		messMap.put("time",format.format(time).toString() );
        		/*mess = "<span style=\"color:red;\">"+from+"</span>对你说: "+message;*/
        		sendMessageToUser(to,new TextMessage(JSON.toJSONString(messMap)));
        		messMap.put("from", null);
        		/*mess = "你对<span style=\"color:red;\">"+to+"</span>说: "+message;*/
        		sendMessageToUser(from,new TextMessage(JSON.toJSONString(messMap)));
            }
       }
    }


	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()){
            webSocketSession.close();
        }
        System.out.println("连接出错，关闭连接......");
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
