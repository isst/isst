package cn.edu.zju.isst.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.UserShowVote;
import cn.edu.zju.isst.pushlet.ShowVoteEventPullSource;

@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowDao showDao;
    @Autowired
    private UserDao userDao;
    
    private static int defaultYear = 2013;

    @Override
    public List<Show> retrieve() {
        return showDao.retrieve(defaultYear);
    }

    @Override
    public ResultHolder vote(int userId, int showId) {
        if (userDao.getUserById(userId) == null) {
            return new ResultHolder("用户不存在");
        }
        
        Show show = showDao.get(showId);
        
        if (show == null) {
            return new ResultHolder("节目不存在");
        }
        
        if (show.getStatus() == 0) {
            return new ResultHolder("节目未开始");
        }
        
        if (showDao.vote(userId, showId)) {
            ShowVoteEventPullSource.startVoting();
            return new ResultHolder();
        }
        
        return new ResultHolder("已投过票");
    }

    @Override
    public Show get(int id) {
        return showDao.get(id);
    }

    @Override
    public int save(Show show) {
        show.setYear(defaultYear);
        if (show.getId() > 0) {
            return showDao.update(show);
        } else {
            return showDao.create(show);
        }
    }

    @Override
    public boolean hasVote(int userId, int showId) {
        return showDao.hasVote(userId, showId);
    }

    @Override
    public List<UserShowVote> retrieveForUser(int userId) {
        return showDao.retrieveForUser(defaultYear, userId);
    }
}
