package zhku.jsj141.ssm.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import zhku.jsj141.ssm.po.Temp;

@Component("mUtils")
public class methodUtils {
	private static Map<String, String> map = null;
	public methodUtils(){
		
	}
	public String OneResultM(String str){
		if(map==null){
			map = new HashMap<String, String>();
		}
		map.put("result", str);
		return JSON.toJSONString(map);
	}
	public String OneResultO(Temp temp){
		return JSON.toJSONString(temp);
	}
	public String resultO(Object obj){
		return JSON.toJSONString(obj);
	}
}
