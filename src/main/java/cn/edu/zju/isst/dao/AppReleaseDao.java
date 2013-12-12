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

import cn.edu.zju.isst.entity.AppRelease;

@Repository
public class AppReleaseDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int create(final AppRelease app) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
           public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
               String sql = "INSERT INTO app_release (type,build,version,url,post_time,readme) VALUES (?,?,?,?,?,?)";
               PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
               ps.setInt(1, app.getType());
               ps.setInt(2, app.getBuild());
               ps.setString(3, app.getVersion());
               ps.setString(4, app.getUrl());
               ps.setLong(5, app.getPostTime());
               ps.setString(6, app.getReadme());
               return ps;
           }
        }, keyHolder);
        
        int id = keyHolder.getKey().intValue();
        app.setId(id);
        return id;
    }
    
    public int update(AppRelease app) {
        String sql = "UPDATE app_release SET type=?,build=?,version=?,url=?,post_time=?,readme=? WHERE id=?";
        
        return jdbcTemplate.update(sql, new Object[] {
                app.getType(), app.getBuild(), app.getVersion(), app.getUrl(), app.getPostTime(), app.getReadme(), app.getId()
        });
    }
    
    public AppRelease getLatestVersion(int type) {
        String sql = "SELECT * FROM app_release WHERE type=? ORDER BY build DESC LIMIT 1";
        List<AppRelease> apps = jdbcTemplate.query(sql, new Object[] { type }, ParameterizedBeanPropertyRowMapper.newInstance(AppRelease.class));
        if (apps.isEmpty()) {
            return null;
        }

        return apps.get(0);
    }
}
