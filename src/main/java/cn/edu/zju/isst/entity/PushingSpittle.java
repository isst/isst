package cn.edu.zju.isst.entity;

import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.User;

public class PushingSpittle {
    private int userId;
    private int spittleId;
    private String userName;
    private String nickname;
    private String content;
    private long postTime;
    private int likes;
    private int dislikes;
    
    public PushingSpittle() {
        
    }
    
    public PushingSpittle(User user, Spittle spittle) {
        userId = user.getId();
        userName = user.getName();
        nickname = user.getNickname();
        spittleId = spittle.getId();
        content = spittle.getContent();
        postTime = spittle.getPostTime();
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpittleId() {
        return spittleId;
    }

    public void setSpittleId(int spittleId) {
        this.spittleId = spittleId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
