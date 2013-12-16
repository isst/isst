package cn.edu.zju.isst.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.UserSpittle;

@Service
public class SpittleServiceImpl implements SpittleService {
    @Autowired
    private SpittleDao spittleDao;
    @Autowired
    private UserDao userDao;
    private static int defaultYear = 2013;
    private static long maxPostInterval = 5000;

    private static Map<Integer, Long> userLastPostTimes = new HashMap<Integer, Long>();
    
    @Override
    public List<UserSpittle> retrieve(int userId, String order, int page, int pageSize, int id) {
        return spittleDao.retrieve(userId, order, page, pageSize, id);
    }

    @Override
    public ResultHolder post(int userId, String content) {
        if (userDao.getUserById(userId) == null) {
            return new ResultHolder("用户不存在");
        }
        
        if (content == null || content.length() < 5 || content.length() > 200) {
            return new ResultHolder("评论内容必须在5~200个字符之间");
        }
        
        Long lastPostTime = userLastPostTimes.get(userId);
        long currentTimeMillis = System.currentTimeMillis();
        if (null != lastPostTime && currentTimeMillis - lastPostTime < maxPostInterval) {
            return new ResultHolder("发布过于频繁");
        }
        
        Spittle spittle = new Spittle();
        spittle.setUserId(userId);
        spittle.setContent(content);
        spittle.setPostTime(currentTimeMillis);
        spittle.setYear(defaultYear);
        spittle.setLikes(0);
        spittle.setDislikes(0);
        int id = spittleDao.create(spittle);
        if (id > 0) {
            userLastPostTimes.put(userId, currentTimeMillis);
            return new ResultHolder(id);
        } else
            return new ResultHolder(0);
    }
    
    @Override
    public ResultHolder delete(int userId, int spittleId) {
        Spittle spittle = spittleDao.get(spittleId);
        if (spittle.getUserId() == userId) {
            spittleDao.delete(spittleId);
            return new ResultHolder();
        } else {
            return new ResultHolder("无权限删除");
        }
    }

    @Override
    public ResultHolder like(int userId, int spittleId, int isLike) {
        if (spittleDao.like(userId, spittleId, isLike)) {
            return new ResultHolder();
        } else {
            return new ResultHolder("请勿重复操作");
        }
    }

    @Override
    public Spittle get(int id) {
        return spittleDao.get(id);
    }
}
