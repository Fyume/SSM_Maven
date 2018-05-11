package zhku.jsj141.ssm.po;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import zhku.jsj141.ssm.validation.ValidGroup1;

public class User {
	@Size(min=2,max=20,message="111",groups={ValidGroup1.class})
	private String uid;
	private String username;
	private String password;
	@NotNull(message="222")
	private String email;
	private String code;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password="
				+ password + ", email=" + email + ", code=" + code + "]";
	}
}
