package cn.edu.zju.isst.entity;

public class SpittleLikeCrazyUser {
    private int userId;
    private String name;
    private String nickname;
    private String fullname;
    private int count;

    public SpittleLikeCrazyUser(User user, int count) {
        userId = user.getId();
        name = user.getName();
        nickname = user.getNickname();
        fullname = user.getFullname();
        this.count = count;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
