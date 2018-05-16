# SSM_test

------

**前言**

哈哈 又是练习项目，就用了几天看的视频。速成，练练手

---------------

**大致想法**

想做一下没做过的 websocket，不然都是什么什么管理系统 想想就郁闷

不用jsp了，用html，前后端分离下吧，el太乱了

（虽然还是不想用框架（angularjs也就上过选修课的程度））:sweat:

结果发现websocket即时通信的话 根本除了登录，不需要操作数据库...又不想帮他们保存聊天记录。。。所以webSocket还是只当一个小功能插件之类的东西算了。

再想一个题目。

-------------------

暂定需求：

注册可进

激活可使用（拦截器咯）

------

**开发记录＆收获**

哇 果然是个坑，连找个可以实现的模版都找半天。先贴个地址：

https://blog.csdn.net/qq_28988969/article/details/76057789

以及

https://blog.csdn.net/dong_19890208/article/details/53741927

（假如连接上了但是立刻断了）这个是跨域请求？

反正我理解的大致流程就是：

> **1.jar包添加**
>
> spring-websocket,spring-messaging
>
> **2.握手拦截器类**
>
> （这个拦截器我好像还没用到，毕竟websocketSession和http那个是不同的）
>
> ```java
> public class MyHandshakeInterceptor implements HandshakeInterceptor{
> 	  public void afterHandshake(ServerHttpRequest request,  
> 	          ServerHttpResponse response, WebSocketHandler wsHandler,  
> 	          Exception ex) {
> 	       System.out.println("After Handshake");  
> 	  }
> 	
> 	  public boolean beforeHandshake(ServerHttpRequest request,  
> 	          ServerHttpResponse response, WebSocketHandler wsHandler,  
> 	          Map<String, Object> attributes) throws Exception {
> 	       System.out.println("Before Handshake");  
> 	       return true;  
> 	  }  
> 	} 
> ```
> **3.url映射配置类**
> ```java
> 	@Configuration
> 	@EnableWebSocket
> 	public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
> 	  @Autowired
> 	  private WebSocketCtrl myHander;
> 		
> 	  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
> 	     // 添加处理器
> 	     //.setAllowedOrigins("*")这个很关键。。。。(跨域请求)
> 	     //"webSocket.do"中".do"因为dispartcherServlet的拦截要求
> 	     registry.addHandler(myHander, "/webSocket.do").addInterceptors(new MyHandshakeInterceptor()).setAllowedOrigins("*");
> 	    }
> 	}
> ```
> **4.处理器（ctrl）**
> ```java
> 	public class WebSocketCtrl implements WebSocketHandler{
> 	    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
> 	    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
> 	      System.out.println("链接关闭......" + closeStatus.toString());
> 	      users.remove(webSocketSession);
> 	    }
> 	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
> 	      System.out.println("链接成功......");
> 	      users.add(session);
> 	      session.sendMessage(new TextMessage("欢迎使用测试聊天平台~~"));
> 	    }
> 	    //这个就是前端调用webSocket.send的时候响应的方法
> 	    //webSocketMessage.getPayload()就是send里面的内容
> 	    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
> 	    	//这个map就是用来存放数据的，以便用来判断用户
> 	   		Map<String, Object> map = session.getAttributes();
> 	        String jsonStr = (String) webSocketMessage.getPayload();
> 	        JSONObject json = JSONObject.parseObject(jsonStr);
> 	        if(json.get("uid")!=null){
> 	        	 String uid = (String)json.get("uid");
> 	        	 map.put("uid", uid);
> 	        }else{
> 	        	 String from = (String) json.get("from");//
> 	             String to = (String) json.get("to");
> 	             String message = (String) json.get("message");
> 	             map.put("from", from);
> 	             map.put("to", to);
> 	             map.put("message", message);
> 	             mess = "<span style=\"color:red;\">"+from+"</span>对你说: "+message;
> 	             sendMessageToUser(to,new TextMessage(mess));
> 	             mess = "你对<span style=\"color:red;\">"+from+"</span>说: "+message;
> 	             sendMessageToUser(from,new TextMessage(mess));
> 	        }
> 	    }
> 	    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
> 	    if(webSocketSession.isOpen()){
> 	        webSocketSession.close();
> 	    }
> 	    System.out.println("链接出错，关闭链接......");
> 	    users.remove(webSocketSession);
> 	   }
> 	   //还没用到
> 	   public boolean supportsPartialMessages() {
> 		return false;
> 	   }
> ```
> 自定义部分：
>
> ```java
> 	  /**
> 	   * 给所有在线用户发送消息
> 	   *
> 	   * @param message
> 	   */
> 	  @RequestMapping("/sendMessToAll")
> 	  public void sendMessageToUsers(TextMessage message) {
> 	      for (WebSocketSession user : users) {
> 	          try {
> 	              if (user.isOpen()) {
> 	                  user.sendMessage(message);
> 	              }
> 	          } catch (IOException e) {
> 	              e.printStackTrace();
> 	          }
> 	      }
> 	  }
> 	  /**
> 	   * 给某个用户发送消息
> 	   *
> 	   * @param userName
> 	   * @param message
> 	   */
> 	  @RequestMapping("/sendMessToOne")
> 	  public void sendMessageToUser(String uid, TextMessage message) {
> 	      for (WebSocketSession user : users) {
> 	          if (user.getAttributes().get("uid").equals(uid)) {
> 	              try {
> 	                  if (user.isOpen()) {
> 	                      user.sendMessage(message);
> 	                  }
> 	              } catch (IOException e) {
> 	                  e.printStackTrace();
> 	              }
> 	              break;
> 	          }
> 	      }
> 	  }
> 	}
> ```
> **5.前端js**
> ```javaScript
> 	function getCon(){//创建连接并配置各个状态的回调
> 	if (typeof WebSocket != 'undefined') {
> 		setMessageInnerHTML("连接中....");
> 		websocket = new WebSocket("ws://localhost:8080/SSM_Maven/webSocket.do");
> 	}else{
> 		alert('该浏览器不支持websocket,建议更换浏览器');
> 	}
> 	websocket.onmessage = function(event) {
> 		$("#returnMessage").html($("#returnMessage").html()+event.data +"<br/>");
> 	};
> 	websocket.onerror = function(){
> 		setMessageInnerHTML("连接失败");
> 	};
> 	websocket.onopen = function(event){
> 		setMessageInnerHTML("连接创建成功....");
> 		var json = {
> 			uid : $("#uid").html()
> 		}
> 		websocket.send(JSON.stringify(json));
> 	};
> 	websocket.onclose = function(){
> 	   setMessageInnerHTML("连接已关闭");
> 	 };
> 	//关闭窗口前先确保关闭了socket连接
> 	window.onbeforeunload = function(){
> 		websocket.close();
> 	  };
> 	}
> ```
> //将消息显示在网页上 
> ```javaScript
> function setMessageInnerHTML(innerHTML){
> $("#returnMessage").html($("#returnMessage").html()+innerHTML +"<br/>");
> }
> ```
> //手动关闭连接
> ```javaScript
> function closeWebSocket(){
> websocket.close();
> }
> ```
> //发送消息
> ```javaScript
> function send(){
> //接收者名称
> var to = $("#toID").val();
> if('' == to){
> alert("请填写接收者");
> return false;
> }
> //发送的消息
> var message = $("#message").val();
> if('' == message){
> alert("请填写发送信息");
> return false;
> }
> var json = {
> 		from:$("#uid").html(),
> 		to:to,
> 		message:message
> }
> var str = JSON.stringify(json)
> websocket.send(str);
> }
> ```





