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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.util.StringUtils;

@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String passwordSalt = "poaerq~!";
    private Map<Integer, User> cachedUsers = new HashMap<Integer, User>();

    public List<User> retrieve(int type) {
        String sql = "SELECT * FROM user WHERE type=? ORDER BY id ASC";
        return jdbcTemplate.query(sql, new Object[] { type }, getUserRowMapper());
    }

    public User getUserByName(String name) {
        String sql = "SELECT * FROM user WHERE name=?";
        List<User> users = jdbcTemplate.query(sql, new Object[] { name }, getUserRowMapper());
        if (users.isEmpty()) {
            return null;
        }

        User user = users.get(0);
        cachedUsers.put(Integer.valueOf(user.getId()), user);

        return user;
    }

    public String encryptPassword(String password) {
        return StringUtils.md5(passwordSalt + password);
    }

    public int create(final User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                String sql = "INSERT INTO user (name, password, fullname, type, nickname) VALUES (?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" });
                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getFullname());
                ps.setInt(4, user.getType());
                ps.setString(5, user.getNickname());
                return ps;
            }
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return id;
    }

    public boolean update(User user) {
        if (user.getId() == 0) {
            return false;
        }
        
        User oldUser = getUserById(user.getId());
        if (oldUser == null) {
            return false;
        }
        
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            user.setPassword(oldUser.getPassword());
        }
        
        String sql = "UPDATE user SET name=?, password=?, fullname=?, type=?, nickname=? WHERE id=?";
        jdbcTemplate.update(sql, new Object[] { user.getName(), user.getPassword(), user.getFullname(), user.getType(),
                user.getNickname(), user.getId() });

        cachedUsers.remove(user.getId());

        return true;
    }

    public User getUserById(int userId) {
        Integer uid = Integer.valueOf(userId);
        if (cachedUsers.containsKey(uid)) {
            return cachedUsers.get(uid);
        }

        String sql = "SELECT * FROM user WHERE id=?";
        List<User> users = jdbcTemplate.query(sql, new Object[] { userId }, getUserRowMapper());

        if (users.isEmpty()) {
            return null;
        }

        User user = users.get(0);
        cachedUsers.put(uid, user);

        return user;
    }

    public boolean updateNickname(int userId, String nickname) {
        String sql = "UPDATE user SET nickname=? WHERE id=?";
        jdbcTemplate.update(sql, new Object[] { nickname, userId });
        cachedUsers.remove(userId);

        return true;
    }

    public boolean userNameExisting(String name) {
        return !checkUserName(name, 0);
    }
    
    public boolean checkUserName(String name, int id) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(id) FROM user WHERE name=?");

        if (id > 0) {
            sql.append(" AND id<>").append(id);
        }

        Integer count = jdbcTemplate.queryForObject(sql.toString(), new Object[] { name }, Integer.class);
        if (count == null || count == 0) {
            return true;
        } else {
            return false;
        }
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
