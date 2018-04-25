package zhku.jsj141.ssm.mapper;

import zhku.jsj141.ssm.po.User;

public interface UserMapper {
	
	public void insertUser(User user) throws Exception;
	
	public User findUser(int uid) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public void deleteUser(int uid) throws Exception;
	
}
