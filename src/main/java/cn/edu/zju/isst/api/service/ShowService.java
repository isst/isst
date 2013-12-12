package cn.edu.zju.isst.api.service;

import java.util.List;

import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.UserShowVote;

public interface ShowService {
    List<Show> retrieve();
    List<UserShowVote> retrieveForUser(int userId);
    Show get(int id);
    ResultHolder vote(int userId, int showId);
    boolean hasVote(int userId, int showId);
    int save(Show show);
}
