package zhku.jsj141.ssm.po;

public class Temp {
	private String title;//temp页面标题
	
	private Object data;//传输的数据
	
	private String result;//返回结果
	
	private String where;//跳转路径
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	
}
