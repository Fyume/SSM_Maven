package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import zhku.jsj141.ssm.utils.JedisUtils;
import zhku.jsj141.ssm.utils.MD5Utils;

public class jedisTest {
	JedisUtils jUtils = null;
	Jedis jedis = null;
	
	@Before
	public void getCon(){
		jUtils = new JedisUtils();
		jedis = jUtils.getJedis();
	}
	@Test
	public void testJ(){
		Map<String, String> map = new HashMap<String,String>();
		/*jedis.del("ggg");
		map.put("aaa", "bbb");
		jedis.hmset("ggg", map);
		jedis.setex("ggg", 2000, "aaa");*/
		map = jedis.hgetAll("ggg");
		System.out.println(map.get("aaa"));
	}
	@Test
	public void gg(){
		Map<String, String> map = new HashMap<String,String>();
		map.put("aaa", "bbb");
		jedis.hmset("ggg", map);
		map = jedis.hgetAll("ggg");
		System.out.println(map.get("aaa"));
	}
	@Test
	public void MD5test(){
		Set<String> keys = jedis.keys(new MD5Utils("fyume").getStr()+"*");
		for (String string : keys) {
			if(jedis.get(string).equals("fyume")){
				/*jedis.del(string);*/
				System.out.println("deleteOK!");
			}
		}
	}
}
