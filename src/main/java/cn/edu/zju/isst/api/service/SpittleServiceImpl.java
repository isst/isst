package cn.edu.zju.isst.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.pushlet.PushingSpittle;
import cn.edu.zju.isst.pushlet.SpittleEventPullSource;

@Service
public class SpittleServiceImpl implements SpittleService {
    @Autowired
    private SpittleDao spittleDao;
    @Autowired
    private UserDao userDao;
    private static int defaultYear = 2013;

    @Override
    public List<Spittle> retrieve(int userId, String order, int page, int pageSize) {
        return spittleDao.retrieve(userId, order, page, pageSize);
    }

    @Override
    public ResultHolder post(int userId, String content) {
        if (userId == 0) {
            return new ResultHolder("用户未登录");
        }
        
        if (content == null || content.length() < 5 || content.length() > 200) {
            return new ResultHolder("评论内容必须在5~200个字符之间");
        }
        
        Spittle spittle = new Spittle();
        spittle.setUserId(userId);
        spittle.setContent(content);
        spittle.setPostTime(System.currentTimeMillis());
        spittle.setYear(defaultYear);
        spittle.setLikes(0);
        spittle.setDislikes(0);
        int id = spittleDao.create(spittle);
        if (id > 0) {
            pushSpittle(spittle);
            return new ResultHolder(id);
        } else
            return new ResultHolder(0);
    }

    private void pushSpittle(Spittle spittle) {
        User user = userDao.getUserById(spittle.getUserId());
        if (user != null) {
            SpittleEventPullSource.jumpQueue(new PushingSpittle(user, spittle));
        }
    }
    
    @Override
    public ResultHolder delete(int userId, int spittleId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultHolder like(int userId, int spittleId, int isLike) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Spittle get(int id) {
        
        return null;
    }
}
