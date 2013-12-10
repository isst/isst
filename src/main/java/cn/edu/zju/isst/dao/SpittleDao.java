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

import cn.edu.zju.isst.entity.Spittle;

@Repository
public class SpittleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Spittle get(int id) {
        String sql = "SELECT * FROM user WHERE id=?";
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

    public List<Spittle> retrieve(int userId, String order, int page, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT * FROM yd_spittle s");
        if (order == null || order.equals("")) {
            order = "post_time";
        }
        
        if (order.equals("post_time")) {
            sql.append(" ORDER BY s.post_time DESC");
        } else if (order.equals("likes")) {
            sql.append(" ORDER BY s.likes DESC");
        } else if (order.equals("dislikes")) {
            sql.append(" ORDER BY s.dislikes DESC");
        }
        
        List<Spittle> spittles = jdbcTemplate.query(sql.toString(), ParameterizedBeanPropertyRowMapper.newInstance(Spittle.class));
        return spittles;
    }
    
    public List<Spittle> retrieve() {
        return retrieve(0, "post_time", 0, 0);
    }
}