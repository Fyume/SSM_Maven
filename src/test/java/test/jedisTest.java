package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
		String uid = new MD5Utils("fyume").getStr();
		System.out.println(uid);
		/*Set<String> keys = jedis.keys(uid+"*");
		for (String string : keys) {
			if(jedis.get(string).equals("fyume")){
				jedis.del(string);
				System.out.println("deleteOK!");
			}
		}*/
	}
	/*@SuppressWarnings("resource")
	@Test
	public void aaa() throws IOException{
		File file = new File("D://姓名和地址.txt");
		InputStream is = new FileInputStream(file);
		byte b[] = new byte[1024];
		int a = is.read(b);
		String str[] = new String(b,0,a).split("");
		int count = 0;
		for(int i = 0;i<str.length;i++){
		if("a".equals(str[i]))count++;
		}
		System.out.println(count);
	}*/
	/*@Test
	public void bbb() throws IOException{
		String str = "adsfadsadfsd";
		StringBuilder str2 = new StringBuilder();
		char[] s = str.toCharArray();
		for (char c : s) {
			if(c!='a'){
				str2.append(c);
			}
		}
		System.out.println(str2);
	}*/
}
