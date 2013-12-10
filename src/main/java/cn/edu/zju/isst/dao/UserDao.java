package cn.edu.zju.isst.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import cn.edu.zju.isst.entity.User;

@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String passwordSalt = "poaerq~!";

    public User getUserByName(String name) {
        String sql = "SELECT * FROM user WHERE name=?";
        List<User> users = jdbcTemplate.query(sql, new Object[] { name }, getUserRowMapper());
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public String encryptPassword(String password) {
        try {
            MessageDigest mdInst;
            mdInst = MessageDigest.getInstance("MD5");
            mdInst.update((passwordSalt + password).getBytes());
            return DigestUtils.md5DigestAsHex(mdInst.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return password;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM user WHERE id=?";
        List<User> users = jdbcTemplate.query(sql, new Object[] { userId }, getUserRowMapper());
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public boolean updateNickname(User user) {
        String sql = "UPDATE user SET nickname=? WHERE id=?";
        int resultInt = jdbcTemplate.update(sql,
                new Object[] { user.getId(), user.getName(), user.getPassword(), user.getType(), user.getFullname(),
                        user.getNickname() });
        if (resultInt > 0) {
            return true;
        }
        return false;
    }

    private RowMapper<User> getUserRowMapper() {
        return new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                final User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setNickname(rs.getString("nickname"));
                user.setFullname(rs.getString("fullname"));
                user.setType(rs.getInt("type"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        };
    }
}
