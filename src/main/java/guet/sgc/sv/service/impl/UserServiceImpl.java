package guet.sgc.sv.service.impl;

import guet.sgc.sv.dao.UserMapper;
import guet.sgc.sv.entity.User;
import guet.sgc.sv.entity.UserExample;
import guet.sgc.sv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(String username, String userType) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andUserTypeEqualTo(userType);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size() != 0){
            return userList.get(0);
        }
        return null;
    }

    @Transactional
    @Override
    public void editUser(User saveUser) {
        userMapper.updateByPrimaryKeySelective(saveUser);
    }
}
