package zhku.jsj141.ssm.utils;

import java.io.File;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
@Component
public class JedisUtils {
	private static Jedis jedis;
	public void openExe() {//启动redis
	    final Runtime runtime = Runtime.getRuntime();  
	    File dir = new File("F:\\redis\\Redis-x64-3.2.100");
	    Process process = null;  
	    try {  
	        process = runtime.exec("F:\\redis\\Redis-x64-3.2.100\\redis-server_startup.bat", null, dir);
	    } catch (final Exception e) {  
	        System.out.println("Error exec!");  
	    }  
	}
	public void connectRedis(){
		jedis = new Jedis("127.0.0.1", 6379);//地址，端口
		jedis.auth("redis");//访问密码
	}
	public JedisUtils(){
		openExe();//启动
		connectRedis();//连接
		System.out.println("创建Redis连接！");
	}
	public Jedis getJedis(){//获取redis
		System.out.println("获取jedis!");
		return jedis;
	}
}
