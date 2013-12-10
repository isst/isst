package cn.edu.zju.isst.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import cn.edu.zju.isst.entity.Show;

@Repository
public class ShowDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Show> retrieve(int year) {
        String sql = "SELECT * FROM yd_show WHERE year=? ORDER BY sort_num";
        List<Show> show = jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Show.class));
        return show;
    }
}
