package cn.edu.zju.isst.entity;

public class UserShowVote extends Show {
    private long vote_time;

    public long getVoteTime() {
        return vote_time;
    }

    public void setVoteTime(long voteTime) {
        this.vote_time = voteTime;
    }
}
