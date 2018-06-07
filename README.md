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

**开发记录＆收获、心得**

### **1.websocket**

哇 果然是个坑，连找个可以实现的模版都找半天。先贴个地址：

https://blog.csdn.net/qq_28988969/article/details/76057789

以及

https://blog.csdn.net/dong_19890208/article/details/53741927

（假如连接上了但是立刻断了）这个是跨域请求？

反正我理解的大致流程就是：

> **step1.jar包添加**
>
> spring-websocket,spring-messaging
>
> **step2.握手拦截器类**
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
> **step3.url映射配置类**
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
> **step4.处理器（ctrl）**
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
> **step5.前端js**
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

才想起来java反编译软件。。都不用下载sources文件了（我用的是luyten）

### **2.前后端分离**

#### 2.1登录模块的实现

今天（18-6-4）回来打码，突然发现前后端分离之后，数据的处理就不一样了，我。。。

关于登录模块，我最后纠结出来的方案就是:

redis存放有期限的key-value（7天，（MD5之后的uid+4位时间）-（uid））
(顺便说一下 redis好像只有setex没有hmsetex之类的。所以结果就是我懒得在hashmap插入时间之后又要根据时间间隔操作键值对；而是直接setex设置一个有期限的 键-值 不放集合里。（毕竟我存放的是（token-uid），已知token组成，用keys模糊匹配出来，再比对一下value(也就是uid)是否一致便可以确定是不是该用户的token了，也可以对其增删改了）)

登录->后台验证成功(根据uid模糊查找redis看下有没有key的value和这个uid相同，有则删除，然后添加新的)->生成token存放redis并返回该token以及用户信息->前端将token存放到cookie，userinfo放localStorage。

进入网页->localStorage有数据->加载用户信息页面

​               └->无数据->加载登录页面
除了部分访问放行，其他都要经过PermissionIntercepter->取cookie中的token

->假如redis里面有->放行
└->无->重定向到Temp.html并带了个参clr（通知前端清除cookie中的token）

### 3.mybatis逆向工程

忘记记录一下了，忘了是从哪个github上面弄得mybatis-generate了（在此对这位大神说声抱歉）
反正在网上也有模版下载
其他参考过的教程地址：

https://blog.csdn.net/abcd898989/article/details/51316612
1.配置需要的jar包
2.generatorConfig.xml配置（也有模版（当然，连接数据库那部分自己改一下，或者加载一个db.properties））
这里给个详细的解说链接：

https://www.cnblogs.com/swugogo/p/7995391.html（其实我都是默认配置的。）
3.创建和开发项目结构一样的包结构（方便直接复制过去），并在generatorConfig.xml里面配置你对应的mapper啊实体类啊的生成位置即可
4.java主执行用文件：这里贴一下代码吧

> ```java
> public class GenMain {  
>     public static void main(String[] args) {  
>         List<String> warnings = new ArrayList<String>();  
>         boolean overwrite = true;  
>         String genCfg = "/generatorConfig.xml";//最主要的配置文件
>         File configFile = new File(GenMain.class.getResource(genCfg).getFile());  
>         ConfigurationParser cp = new ConfigurationParser(warnings);  
>         Configuration config = null;  
>         try {  
>             config = cp.parseConfiguration(configFile);  
>         } catch (IOException e) {  
>             e.printStackTrace();  
>         } catch (XMLParserException e) {  
>             e.printStackTrace();  
>         }  
>         DefaultShellCallback callback = new DefaultShellCallback(overwrite);  
>         MyBatisGenerator myBatisGenerator = null;  
>         try {  
>             myBatisGenerator = new MyBatisGenerator(config, callback, warnings);//主要执行方法
>             System.out.println("生成完毕！！！！");
>         } catch (InvalidConfigurationException e) {  
>             e.printStackTrace();  
>         }  
>         try {  
>             myBatisGenerator.generate(null);  
>         } catch (SQLException e) {  
>             e.printStackTrace();  
>         } catch (IOException e) {  
>             e.printStackTrace();  
>         } catch (InterruptedException e) {  
>             e.printStackTrace();  
>         }  
>     }  
> }  
> ```

话说一早没学mybatis的时候好像说hibernate也有逆向工程的。啊 hibernate有个好处就是可以配置hbm2ddl.auto为update就可以自动生成表（当然详细的好像是说假如没有表则创建，假如表不一样的修改，假如一样则只插入数据）
 话说 这里吐槽一下 今天插入数据的时候一直报语法错误，我就纳闷了，明明逻辑是对的，然后找了好一阵，insert into 表名（字段，字段）values(值，值);这个有错吗？想了想，好像导出来的sql文件里面插入数据都是insert into 表名（\`字段\`,\`字段\`）values(值，值),然后试了下，发现ok了。。。查了下 原来是保留字冲突。我记得大二数据库那门课的时候也没有说过（用的sqlServer2008）算是涨姿势。