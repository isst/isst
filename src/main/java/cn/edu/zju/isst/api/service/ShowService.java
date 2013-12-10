package cn.edu.zju.isst.api.service;

import java.util.List;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;

public interface ShowService {
    List<Show> retrieve();
    ResultHolder vote(int userId, int showId);
}
