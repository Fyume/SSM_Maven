package zhku.jsj141.ssm.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class mailUtils {

	public static String sendEmail(String emailAddress,String mess) throws MessagingException, IOException{//发送激活链接
		String path = mailUtils.class.getResource("/javaMail.properties").getPath();
		InputStream in = new BufferedInputStream(new FileInputStream(  
                new File(path)));  
		final Properties props = new Properties();  
		props.load(in);
	    // 构建授权信息，用于进行SMTP进行身份验证  
	    Authenticator authenticator = new Authenticator() {  
	        @Override  
	        protected PasswordAuthentication getPasswordAuthentication() {  
	            // 用户名、密码  
	            String userName = props.getProperty("mail.user");  
	            String password = props.getProperty("mail.password");  
	            return new PasswordAuthentication(userName, password);  
	        }  
	    };  
	    // 使用环境属性和授权信息，创建邮件会话  
	    Session mailSession = Session.getInstance(props, authenticator);  
	    // 创建邮件消息  
	    MimeMessage message = new MimeMessage(mailSession);  
	    // 设置发件人  
	    InternetAddress form = new InternetAddress(props.getProperty("mail.user"));  
	    message.setFrom(form);  
	    
	    // 设置收件人  
	    InternetAddress to = new InternetAddress(emailAddress);  
	    message.setRecipient(RecipientType.TO, to);  

	    // 设置邮件标题  
	    message.setSubject("某测试项目的激活邮件");  
	    
	    
	    // 设置邮件的内容体  
	    /*message.setContent("<h1>激活请点击以下链接(有效时间10分钟)</h1><h3>"
	    		+ "<a href='http://47.106.104.150:8080/SSM_Maven/user/activate.do?code="+mess+"'>点击跳转激活</a>"
	    				+ "</h3>", "text/html;charset=UTF-8");*/  
	    message.setContent("<h1>激活请点击以下链接(有效时间10分钟)</h1><h3>"
	    		+ "<a href='http://localhost:8080/SSM_Maven/user/activate.do?code="+mess+"'>点击跳转激活</a>"
	    				+ "</h3>", "text/html;charset=UTF-8");
	    // 发送邮件  
	    Transport.send(message);  
	    return "email_ok";
	}
}
