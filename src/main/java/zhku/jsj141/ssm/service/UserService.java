package zhku.jsj141.ssm.service;

import zhku.jsj141.ssm.po.User;

public interface UserService {
	
	public User findUser(int uid) throws Exception;
}
