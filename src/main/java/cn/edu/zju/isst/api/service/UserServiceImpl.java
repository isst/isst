package cn.edu.zju.isst.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public ResultHolder validate(String name, String password) {
        User user = userDao.getUserByName(name);
        if (user != null && user.getId() > 0) {
            if (user.getPassword().equals(userDao.encryptPassword(password))) {
                return new ResultHolder(user.getId());
            }
            return new ResultHolder("密码错误");
        }
        return new ResultHolder("用户不存在");
    }

    @Override
    public User get(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public ResultHolder updateNickname(int userId, String nickname) {
        if (null == nickname || nickname.equals("")) {
            return new ResultHolder("昵称不能为空");
        }
        
        nickname = nickname.replaceAll("(\r\n|\n)", " ").replaceAll("\\<.*?>","");
        
        if (nickname.length() > 10) {
            return new ResultHolder("昵称不能大于10个字符");
        }
        
        if (userDao.updateNickname(userId, nickname)) {
            return new ResultHolder(userId);
        } else {
            return new ResultHolder(0);
        }
    }
}
