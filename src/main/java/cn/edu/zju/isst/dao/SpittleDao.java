package cn.edu.zju.isst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.entity.UserSpittle;

@Repository
public class SpittleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;
    
    public Spittle get(int id) {
        String sql = "SELECT * FROM yd_spittle WHERE id=?";
        List<Spittle> spittles = jdbcTemplate.query(sql, new Object[] { id }, ParameterizedBeanPropertyRowMapper.newInstance(Spittle.class));
        if (spittles.isEmpty()) {
            return null;
        }
        return spittles.get(0);
    }
    
    public int create(final Spittle spittle) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
           public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
               String sql = "INSERT INTO yd_spittle (year,user_id,content,post_time,likes,dislikes) VALUES (?,?,?,?,?,?)";
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

    public List<UserSpittle> retrieve(int userId, String order, int page, int pageSize, int id) {
        StringBuilder sql = new StringBuilder("SELECT s.*, su.is_like FROM yd_spittle s LEFT JOIN yd_spittle_user su ON s.id=su.spittle_id AND su.user_id=?");
        StringBuilder where = new StringBuilder();
        
        if (order == null || order.equals("")) {
            order = "post_time";
        }
        
        if (id > 0) {
            where.append(" s.id>").append(id);
        }
        
        if (where.length() > 0) {
            sql.append(" WHERE").append(where);
        }
        
        if (order.equals("post_time")) {
            sql.append(" ORDER BY s.post_time DESC");
        } else if (order.equals("likes")) {
            sql.append(" ORDER BY s.likes DESC");
        } else if (order.equals("dislikes")) {
            sql.append(" ORDER BY s.dislikes DESC");
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
        
        return true;
    }
}