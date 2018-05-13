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
		return userMapper.findUser(uid);
	}
}
