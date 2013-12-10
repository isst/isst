package cn.edu.zju.isst.api.service;

import java.util.List;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Spittle;

public interface SpittleService {
    Spittle get(int id);
    List<Spittle> retrieve(int userId, String order, int page, int pageSize);
    ResultHolder post(int userId, String content);
    ResultHolder delete(int userId, int spittleId);
    ResultHolder like(int userId, int spittleId, int isLike);
}
