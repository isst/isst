package cn.edu.zju.isst.api.service;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

public interface UserService {
    ResultHolder validate(String name, String password);
    User get(int userId);
    ResultHolder updateNickname(int userId, String nickname);
    void cleanUserLoggedCounts();
}
