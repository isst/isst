package cn.edu.zju.isst.api.service;

import java.util.List;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.UserSpittle;

public interface SpittleService {
    Spittle get(int id);
    List<UserSpittle> retrieve(int userId, String order, int page, int pageSize, int id);
    ResultHolder post(int userId, String content);
    ResultHolder delete(int userId, int spittleId);
    ResultHolder like(int userId, int spittleId, int isLike);
}
