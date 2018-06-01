package zhku.jsj141.ssm.service;

import zhku.jsj141.ssm.po.User;

public interface UserService {
	
	public User findUser(String uid) throws Exception;
	
	public User findUserByCode(String code) throws Exception;
	
	public int insertSelective(User user) throws Exception;
	
	public int updateSelective(User record) throws Exception;
}
