package cn.edu.zju.isst.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.config.PartyConfig;
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

    private static Map<Integer, Long> userLastPostTimes = new HashMap<Integer, Long>();
    private static Map<Integer, String> userLastPostContents = new HashMap<Integer, String>();
    
    @Override
    public List<UserSpittle> retrieve(int userId, String order, int page, int pageSize, int id) {
        return spittleDao.retrieve(userId, order, page, pageSize, id);
    }

    @Override
    public ResultHolder post(int userId, String content) {
        if (userDao.getUserById(userId) == null) {
            return new ResultHolder("用户不存在");
        }
        
        if (content == null || content.length() < 5 || content.length() > 140) {
            return new ResultHolder("评论内容必须在5~140个字符之间");
        }
        
        content = content.replaceAll("(\r\n|\n)", " ").replaceAll("\\<.*?>","");
        
        Long lastPostTime = userLastPostTimes.get(userId);
        long currentTimeMillis = System.currentTimeMillis();
        if (null != lastPostTime && currentTimeMillis - lastPostTime < PartyConfig.SPITTLE_POST_INTERVAL) {
            return new ResultHolder("发布过于频繁");
        }
        
        String lastContent = userLastPostContents.get(userId);
        if (null != lastContent) {
            if (lastContent.length() == content.length()) {
                if (lastContent.equals(content)) {
                    return new ResultHolder("已发布过相同的内容");
                }
            } else {
                if (lastContent.contains(content) || content.contains(lastContent)) {
                    return new ResultHolder("已发布过相似的内容");
                }
            }
        }
        
        Spittle spittle = new Spittle();
        spittle.setUserId(userId);
        spittle.setContent(content);
        spittle.setPostTime(currentTimeMillis);
        spittle.setLikes(0);
        spittle.setDislikes(0);
        int id = spittleDao.create(spittle);
        if (id > 0) {
            userLastPostTimes.put(userId, currentTimeMillis);
            userLastPostContents.put(userId, content);
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
        if (null ==userDao.getUserById(userId)) {
            return new ResultHolder("用户不存在");
        }
        if (!spittleDao.exist(spittleId)) {
            return new ResultHolder("评论不存在");
        }
        
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
