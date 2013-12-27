package cn.edu.zju.isst.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private Map<Integer, Integer> userLoggedCounts = new HashMap<Integer, Integer>();
    
    @Override
    public ResultHolder validate(String name, String password) {
        User user = userDao.getUserByName(name);
        if (user != null && user.getId() > 0) {
            Integer loggedCount = userLoggedCounts.get(user.getId());
            if (loggedCount == null) {
                loggedCount = 0;
            }
            if (loggedCount > 100) {
                return new ResultHolder("登录错误次数太多了");
            }
            if (user.getPassword().equals(userDao.encryptPassword(password))) {
                return new ResultHolder(user.getId());
            }
            
            userLoggedCounts.put(user.getId(), loggedCount+1);
            return new ResultHolder("密码错误");
        }
        return new ResultHolder("用户不存在");
    }
    
    public void cleanUserLoggedCounts() {
        userLoggedCounts.clear();
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
        
        nickname = nickname.trim();
        nickname = nickname.replaceAll("&([#a-zA-Z0-9]+);", "").replaceAll("\\<.*?>","").replaceAll("\\s", "");
        
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
