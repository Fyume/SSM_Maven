package zhku.jsj141.ssm.mapper;

import org.springframework.stereotype.Repository;

import zhku.jsj141.ssm.po.User;
@Repository
public interface UserMapper {
	
	public void insertUser(User user) throws Exception;
	
	public User findUser(String uid) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public void deleteUser(String uid) throws Exception;
	
}
