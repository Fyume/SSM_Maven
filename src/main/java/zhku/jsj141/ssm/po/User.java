package zhku.jsj141.ssm.po;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class User implements Serializable {
	@NotNull
    private String uid;

    private String username;
    @NotNull
    private String password;

    private String telnum;
    
    private String email;
    @NotNull
    private String email2;

    private String image;

    private String code;

    private Long activatetime;

    private Integer psFalse;

    private Long psTime;

    private String pscode;

    private static final long serialVersionUID = 1L;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum == null ? null : telnum.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2 == null ? null : email2.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getActivatetime() {
        return activatetime;
    }

    public void setActivatetime(Long activatetime) {
        this.activatetime = activatetime;
    }

    public Integer getPsFalse() {
        return psFalse;
    }

    public void setPsFalse(Integer psFalse) {
        this.psFalse = psFalse;
    }

    public Long getPsTime() {
        return psTime;
    }

    public void setPsTime(Long psTime) {
        this.psTime = psTime;
    }

    public String getPscode() {
        return pscode;
    }

    public void setPscode(String pscode) {
        this.pscode = pscode == null ? null : pscode.trim();
    }

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", telnum=" + telnum
				+ ", email=" + email + ", email2=" + email2 + ", image=" + image + ", code=" + code + ", activatetime="
				+ activatetime + ", psFalse=" + psFalse + ", psTime=" + psTime + ", pscode=" + pscode + "]";
	}
    
}