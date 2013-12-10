package cn.edu.zju.isst.pushlet;

import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.User;

public class PushingSpittle {
    private int userId;
    private int spittleId;
    private String nickname;
    private String content;
    private long postTime;

    public PushingSpittle(User user, Spittle spittle) {
        userId = user.getId();
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
    
    public String toString() {
        return nickname + ": " + content;
    }
}
