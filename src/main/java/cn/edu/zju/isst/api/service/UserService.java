package cn.edu.zju.isst.api.service;

import java.util.List;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

public interface UserService {
    ResultHolder validate(String name, String password);
    User get(int userId);
    User get(String username);
    ResultHolder updateNickname(int userId, String nickname);
    void cleanUserLoggedCounts();
    public void disableUser(int userId);
    public void enableUser(int userId);
    public List<User> findDisabled();
}
