package cn.edu.zju.isst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.zju.isst.config.PartyConfig;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.UserShowVote;

@Repository
public class ShowDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static ShowDao instance;

    public ShowDao() {
        instance = this;
    }
    
    public static ShowDao getInstance() {
        return instance;
    }
    
    public Show get(int id) {
        String sql = "SELECT * FROM yd_show WHERE id=?";
        List<Show> shows = jdbcTemplate.query(sql, new Object[] { id }, ParameterizedBeanPropertyRowMapper.newInstance(Show.class));
        if (shows.isEmpty()) {
            return null;
        }

        return shows.get(0);
    }
    
    public List<Show> retrieve() {
        return retrieve(PartyConfig.YEAR);
    }
    
    public List<Show> retrieve(int year) {
        String sql = "SELECT * FROM yd_show WHERE year=? ORDER BY sort_num";
        return jdbcTemplate.query(sql, new Object[] {year}, ParameterizedBeanPropertyRowMapper.newInstance(Show.class));
    }
    
    public List<UserShowVote> retrieveForUser(int userId) {
        return retrieveForUser(PartyConfig.YEAR, userId);
    }
    
    public List<UserShowVote> retrieveForUser(int year, int userId) {
        String sql = "SELECT s.*, sv.post_time vote_time FROM yd_show s LEFT JOIN yd_show_vote sv ON s.id=sv.show_id AND sv.user_id=? WHERE s.year=? ORDER BY s.sort_num";
        return jdbcTemplate.query(sql, new Object[] {userId, year}, new RowMapper<UserShowVote>() {
            @Override
            public UserShowVote mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserShowVote show = new UserShowVote();
                show.setId(rs.getInt("id"));
                show.setCategory(rs.getString("category"));
                show.setName(rs.getString("name"));
                show.setOrganization(rs.getString("organization"));
                show.setSortNum(rs.getInt("sort_num"));
                show.setStatus(rs.getInt("status"));
                show.setYear(rs.getInt("year"));
                Long voteTime = rs.getLong("vote_time");
                if (null == voteTime) {
                    show.setVoteTime(0);
                } else {
                    show.setVoteTime(voteTime.longValue());
                }
                return show;
            }
            
        });
    }
    
    public int create(final Show show) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
           public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
               String sql = "INSERT INTO yd_show (year,name,organization,category,status,sort_num) VALUES (?,?,?,?,?,?)";
               PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
               ps.setInt(1, show.getYear());
               ps.setString(2, show.getName());
               ps.setString(3, show.getOrganization());
               ps.setString(4, show.getCategory());
               ps.setInt(5, show.getStatus());
               ps.setInt(6, show.getSortNum());
               return ps;
           }
        }, keyHolder);
        
        int id = keyHolder.getKey().intValue();
        show.setId(id);
        return id;
    }
    
    public int update(Show show) {
        String sql = "UPDATE yd_show SET year=?,name=?,organization=?,category=?,status=?,sort_num=? WHERE id=?";
        
        return jdbcTemplate.update(sql, new Object[] {
                show.getYear(), show.getName(), show.getOrganization(), show.getCategory(), show.getStatus(), show.getSortNum(), show.getId()
        });
    }
    
    @Transactional
    public int vote(int userId, int showId) {
        if (hasVote(userId, showId)) {
            return 1;
        }
        
        String sql = "INSERT INTO yd_show_vote (user_id, show_id, post_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {userId, showId, System.currentTimeMillis()/1000});
        
        return 0;
    }
    
    public boolean hasVote(int userId, int showId) {
        String countSQL = "SELECT COUNT(id) FROM yd_show_vote WHERE user_id=? AND show_id=?";
        int count = jdbcTemplate.queryForObject(countSQL, new Object[] {userId, showId}, Integer.class);
        if (count > 0) {
            return true;
        }
        return false;
    }
    
    public Map<Integer, Integer> statisticalVote() {
        String sql = "SELECT show_id, COUNT(id) as votes FROM yd_show_vote GROUP BY show_id";
        return jdbcTemplate.query(sql, new ResultSetExtractor<Map<Integer, Integer>>() {
            @Override
            public Map<Integer, Integer> extractData(ResultSet rs) throws SQLException {
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                while(rs.next()) {
                    map.put(rs.getInt("show_id"), rs.getInt("votes"));
                }
                return map;
            }
        });
    }
}
