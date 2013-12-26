package cn.edu.zju.isst.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.config.PartyConfig;
import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.entity.UserShowVote;
import cn.edu.zju.isst.pushlet.ShowVoteEventPullSource;

@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowDao showDao;
    @Autowired
    private UserDao userDao;

    private Map<Integer, Integer> userVotes = new HashMap<Integer, Integer>();
    
    @Override
    public List<Show> retrieve() {
        return showDao.retrieve();
    }

    @Override
    public ResultHolder vote(int userId, int showId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            return new ResultHolder("用户不存在");
        }
        
        Show show = showDao.get(showId);
        if (show == null) {
            return new ResultHolder("节目不存在");
        }
        
        if (show.getStatus() == 0) {
            return new ResultHolder("节目未开始");
        }
        
        Integer votes = userVotes.get(userId);
        if (votes == null) {
            votes = showDao.getUserVotes(userId);
        }
        
        int maxVotes = user.getType() == 0 ? PartyConfig.STUDENT_MAX_VOTES : PartyConfig.TEACHER_MAX_VOTES;
        if (maxVotes > 0 && votes >= maxVotes) {
            return new ResultHolder("最多只能投" + maxVotes + "票");
        }
        
        int result = showDao.vote(userId, showId);
        if (result == 0) {
            userVotes.put(userId, ++votes);
            //ShowVoteEventPullSource.startVoting();
            return new ResultHolder();
        } else if (result == 1) {
            return new ResultHolder("已投过票");
        }
        
        return null;
    }

    @Override
    public Show get(int id) {
        return showDao.get(id);
    }

    @Override
    public int save(Show show) {
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
        return showDao.retrieveForUser(userId);
    }
}
