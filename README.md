# SSM_test

------

**前言**

哈哈 又是练习项目，就用了几天看的视频。速成，练练手

想做一下没做过的 websocket，不然都是什么什么管理系统 想想就郁闷

---------------

不用jsp了，html，前后端分离下吧（虽然还是不想用框架（angularjs也就上过选修课的程度））

------

**开发记录**

主页套了个模版

------

**收获**

哇 果然是个坑，连找个可以实现的模版都找半天。先贴个地址：

https://blog.csdn.net/qq_28988969/article/details/76057789

以及

https://blog.csdn.net/dong_19890208/article/details/53741927（假如连接上了但是立刻断了）这个是跨域请求？

大致就是

**1.jar包添加**

spring-websocket,spring-messaging

**2.握手拦截器类**

	public class MyHandshakeInterceptor implements HandshakeInterceptor{
	  public void afterHandshake(ServerHttpRequest request,  
	          ServerHttpResponse response, WebSocketHandler wsHandler,  
	          Exception ex) {
	       System.out.println("After Handshake");  
	  }
	
	  public boolean beforeHandshake(ServerHttpRequest request,  
	          ServerHttpResponse response, WebSocketHandler wsHandler,  
	          Map<String, Object> attributes) throws Exception {
	       System.out.println("Before Handshake");  
	       return true;  
	  }  
	}  

**3.url映射配置类**

	@Configuration
	@EnableWebSocket
	public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	  @Autowired
	  private WebSocketCtrl myHander;
		
	  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	     // 添加处理器
	     //.setAllowedOrigins("*")这个很关键。。。。(跨域请求)
	     //"webSocket.do"中".do"因为dispartcherServlet的拦截要求
	     registry.addHandler(myHander, "/webSocket.do").addInterceptors(new MyHandshakeInterceptor()).setAllowedOrigins("*");
	    }
	}
**4.处理器（ctrl）**

	public class WebSocketCtrl implements WebSocketHandler{
	    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
	    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
	      System.out.println("链接关闭......" + closeStatus.toString());
	      users.remove(webSocketSession);
	    }
	    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
	      System.out.println("链接关闭......" + closeStatus.toString());
	      users.remove(webSocketSession);
	    }
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	      System.out.println("链接成功......");
	      users.add(session);
	      String userName = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
	      if(userName!= null){
	          //查询未读消息
	          int count = 5;
	          session.sendMessage(new TextMessage(count + ""));
	      }
	    }
	    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
	    sendMessageToUsers(new TextMessage(webSocketMessage.getPayload() + ""));
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
自定义部分：


	  /**
	   * 给所有在线用户发送消息
	   *
	   * @param message
	   */
	  @RequestMapping("/sendMessToAll")
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
	  @RequestMapping("/sendMessToOne")
	  public void sendMessageToUser(String userName, TextMessage message) {
	      for (WebSocketSession user : users) {
	          if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(userName)) {
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
**5.前端js**







