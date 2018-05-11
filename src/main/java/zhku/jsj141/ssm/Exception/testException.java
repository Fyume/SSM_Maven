package zhku.jsj141.ssm.Exception;

public class testException extends Exception {
	public String message;//异常信息
	
	public testException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
