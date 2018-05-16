package zhku.jsj141.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhku.jsj141.ssm.mapper.UserMapper;
import zhku.jsj141.ssm.po.User;
import zhku.jsj141.ssm.service.UserService;
@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;
	
	public User findUser(String uid) throws Exception{
		return userMapper.selectByPrimaryKey(uid);
	}
	public int insertSelective(User record) throws Exception{
		return userMapper.insertSelective(record);
	}
}
