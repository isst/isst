package cn.edu.zju.isst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.zju.isst.config.PartyConfig;
import cn.edu.zju.isst.entity.LotterySpittle;
import cn.edu.zju.isst.entity.PushingSpittle;
import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.SpittleLikeCrazyUser;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.entity.UserSpittle;

@Repository
public class SpittleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;
    
    private static SpittleDao instance;
    
    public SpittleDao() {
        instance = this;
    }
    
    public static SpittleDao getInstance() {
        return instance;
    }
    
    public boolean exist(int id) {
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM yd_spittle WHERE id=?", new Object[] {id}, Integer.class) > 0 ? true : false;
    }
    
    public Spittle get(int id) {
        String sql = "SELECT * FROM yd_spittle WHERE id=?";
        List<Spittle> spittles = jdbcTemplate.query(sql, new Object[] { id }, ParameterizedBeanPropertyRowMapper.newInstance(Spittle.class));
        if (spittles.isEmpty()) {
            return null;
        }
        return spittles.get(0);
    }
    
    public List<Spittle> get(int[] ids) {
        StringBuilder sql = new StringBuilder("SELECT * FROM yd_spittle WHERE id IN (");
        sql.append(ids[0]);
        for (int i=1; i<ids.length; i++) {
            sql.append(",").append(ids[i]);
        }
        sql.append(")");
        List<Spittle> spittles = jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(Spittle.class));
        return spittles;
    }
    
    @Transactional
    public PushingSpittle getPushingSpittle() {
        String sql = "SELECT * FROM yd_spittle WHERE is_display=? ORDER BY likes_diff DESC, id ASC LIMIT 0, 1";
        List<PushingSpittle> spittles = jdbcTemplate.query(sql, new Object[] {0}, getPushingSpittleRowMapper());
        PushingSpittle pushingSpittle = null;
        if (spittles.isEmpty()) {
            pushingSpittle = getRandomPushingSpittle();
        } else {
            pushingSpittle = spittles.get(0);
            String updateSql = "UPDATE yd_spittle SET is_display=1 WHERE id=?";
            jdbcTemplate.update(updateSql, new Object[] {pushingSpittle.getSpittleId()});
        }
        
        return pushingSpittle;
    }
    
    private PushingSpittle getRandomPushingSpittle() {
        String sql = "SELECT * FROM yd_spittle t1 JOIN (SELECT FLOOR(RAND()*((SELECT MAX(id) FROM yd_spittle)-(SELECT MIN(id) FROM yd_spittle))+(SELECT MIN(id) FROM yd_spittle)) id) t2 WHERE t1.id >= t2.id ORDER BY t1.id LIMIT 1";
        List<PushingSpittle> spittles = jdbcTemplate.query(sql, getPushingSpittleRowMapper());
        if (spittles.isEmpty()) {
            return null;
        }
        return spittles.get(0);
    }
    
    public List<LotterySpittle> retrieveLotterySpittles() {
        String sql = "SELECT s.*, COUNT(s.id) spittle_count, SUM(likes) spittle_likes FROM (SELECT * FROM  yd_spittle ORDER BY likes DESC) s WHERE user_id NOT IN (SELECT DISTINCT user_id FROM yd_spittle_lottery) GROUP BY user_id ORDER BY RAND()";
        return jdbcTemplate.query(sql, new RowMapper<LotterySpittle>() {
            @Override
            public LotterySpittle mapRow(ResultSet rs, int rowNum) throws SQLException {
                LotterySpittle ls = new LotterySpittle();
                ls.setSpittleId(rs.getInt("id"));
                ls.setContent(rs.getString("content"));
                ls.setPostTime(rs.getLong("post_time"));
                ls.setUserId(rs.getInt("user_id"));
                ls.setLikes(rs.getInt("likes"));
                ls.setDislikes(rs.getInt("dislikes"));
                int spittleCount = rs.getInt("spittle_count");
                int spittleLikes = rs.getInt("spittle_likes");
                int weight = (int) (Math.round(Math.log(spittleCount+spittleLikes) / Math.log(2)));
                ls.setWeight(weight);
                User user = userDao.getUserById(rs.getInt("user_id"));
                if (user != null) {
                    ls.setUserName(user.getName());
                    ls.setNickname(user.getNickname());
                    ls.setFullname(user.getFullname());
                }
                return ls;
            }
        });
    }
    
    public List<PushingSpittle> retrievePushingSpittles() {
        String sql = "SELECT * FROM yd_spittle";
        return jdbcTemplate.query(sql, getPushingSpittleRowMapper());
    }
    
    private RowMapper<PushingSpittle> getPushingSpittleRowMapper() {
        return new RowMapper<PushingSpittle>() {
            @Override
            public PushingSpittle mapRow(ResultSet rs, int rowNum) throws SQLException {
                PushingSpittle ps = new PushingSpittle();
                ps.setSpittleId(rs.getInt("id"));
                ps.setContent(rs.getString("content"));
                ps.setPostTime(rs.getLong("post_time"));
                ps.setUserId(rs.getInt("user_id"));
                ps.setLikes(rs.getInt("likes"));
                ps.setDislikes(rs.getInt("dislikes"));
                User user = userDao.getUserById(rs.getInt("user_id"));
                if (user != null) {
                    ps.setUserName(user.getName());
                    ps.setNickname(user.getNickname());
                }
                return ps;
            }
        };
    }
    
    public int create(final Spittle spittle) {
        spittle.setYear(PartyConfig.YEAR);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
           public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
               String sql = "INSERT INTO yd_spittle (year,user_id,content,post_time,likes,dislikes,likes_diff) VALUES (?,?,?,?,?,?,0)";
               PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
               ps.setInt(1, spittle.getYear());
               ps.setInt(2, spittle.getUserId());
               ps.setString(3, spittle.getContent());
               ps.setLong(4, spittle.getPostTime() / 1000);
               ps.setInt(5, spittle.getLikes());
               ps.setInt(6, spittle.getDislikes());
               return ps;
           }
        }, keyHolder);
        
        int id = keyHolder.getKey().intValue();
        spittle.setId(id);
        return id;
    }
    
    public void winPrize(int[] spittleIds, final int prizeType) {
        List<Spittle> spittles = get(spittleIds);
        
        if (spittles != null) {
            for (final Spittle spittle : spittles) {
                jdbcTemplate.update(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                        String sql = "INSERT INTO yd_spittle_lottery (year,user_id,spittle_id,post_time,prize_type) VALUES (?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
                        ps.setInt(1, spittle.getYear());
                        ps.setInt(2, spittle.getUserId());
                        ps.setInt(3, spittle.getId());
                        ps.setLong(4, System.currentTimeMillis()/1000);
                        ps.setInt(5, prizeType);
                        return ps;
                    }
                 });
            }
        }
    }

    public List<UserSpittle> retrieve(int userId, String order, int page, int pageSize, int id) {
        StringBuilder sql = new StringBuilder("SELECT s.*, su.is_like FROM yd_spittle s LEFT JOIN yd_spittle_user su ON s.id=su.spittle_id AND su.user_id=?");
        StringBuilder where = new StringBuilder();
        
        if (order == null || order.equals("")) {
            order = "post_time";
        }
        
        if (id > 0) {
            where.append(" s.id<=").append(id);
        }
        
        if (where.length() > 0) {
            sql.append(" WHERE").append(where);
        }
        
        if (order.equals("post_time")) {
            sql.append(" ORDER BY s.post_time DESC");
        } else if (order.equals("likes")) {
            sql.append(" ORDER BY s.likes DESC, s.dislikes ASC");
        } else if (order.equals("dislikes")) {
            sql.append(" ORDER BY s.dislikes DESC, s.likes DESC");
        }
        
        int offset = page == 0 ? 0 : ((page - 1) * pageSize);
        sql.append(" LIMIT ").append(offset).append(", ").append(pageSize);
        List<UserSpittle> spittles = jdbcTemplate.query(sql.toString(), new Object[] {userId}, ParameterizedBeanPropertyRowMapper.newInstance(UserSpittle.class));
        for (UserSpittle spittle : spittles) {
            User user = userDao.getUserById(spittle.getUserId());
            if (null != user) {
                spittle.setNickname(user.getNickname());
            }
        }
        
        return spittles;
    }
    
    public List<Spittle> retrieve(int count, String order, boolean desc) {
        StringBuilder sb = new StringBuilder("SELECT * FROM yd_spittle ORDER BY ");
        sb.append(order).append(" ").append(desc ? "DESC" : "ASC");
        
        if (count > 0) {
            sb.append(" LIMIT ").append(count);
        }
        
        return jdbcTemplate.query(sb.toString(), ParameterizedBeanPropertyRowMapper.newInstance(Spittle.class));
    }
    
    @Transactional
    public boolean delete(int spittleId) {
        jdbcTemplate.update("DELETE FROM yd_spittle WHERE id=?", new Object[] {spittleId});
        jdbcTemplate.update("DELETE FROM yd_spittle_user WHERE spittle_id=?", new Object[] {spittleId});
        
        return true;
    }
    
    @Transactional
    public boolean like(int userId, int spittleId, int isLike) {
        String countSQL = "SELECT COUNT(id) FROM yd_spittle_user WHERE user_id=? AND spittle_id=?";
        int count = jdbcTemplate.queryForObject(countSQL, new Object[] {userId, spittleId}, Integer.class);
        if (count > 0) {
            return false;
        }
        
        String sql = "INSERT INTO yd_spittle_user (user_id, spittle_id, is_like) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {userId, spittleId, isLike});
        
        StringBuilder updateSql = new StringBuilder("UPDATE yd_spittle SET ");
        String updateField = isLike == 0 ? "dislikes" : "likes";
        updateSql.append(updateField).append("=").append(updateField).append("+1");
        updateSql.append(", is_display='0', likes_diff=likes-dislikes WHERE id=?");
        jdbcTemplate.update(updateSql.toString(), new Object[] {spittleId});
        
        return true;
    }
    
    public List<SpittleLikeCrazyUser> getLikeCrazyUser(boolean isLike, int top) {
        String sql = "SELECT COUNT(id) likes_count, user_id FROM `yd_spittle_user` WHERE is_like=? GROUP BY user_id ORDER BY likes_count DESC LIMIT " + top;
        return jdbcTemplate.query(sql, new Object[] {isLike ? 1 : 0}, new RowMapper<SpittleLikeCrazyUser>() {
            @Override
            public SpittleLikeCrazyUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SpittleLikeCrazyUser(userDao.getUserById(rs.getInt("user_id")), rs.getInt("likes_count"));
            }
        });
    }
    
    public List<SpittleLikeCrazyUser> getLikeCrazyUser(int top) {
        return getLikeCrazyUser(true, top);
    }
    
    public List<SpittleLikeCrazyUser> getDislikeCrazyUser(int top) {
        return getLikeCrazyUser(false, top);
    }
}