package cn.edu.zju.isst.entity;

public class UserSpittle extends Spittle {
    private String nickname;
    private int is_disliked = 0;
    private int is_liked = 0;
   
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIsDisliked() {
        return is_disliked;
    }

    public void setIsDisliked(int isDisliked) {
        this.is_disliked = isDisliked;
    }

    public int getIsLiked() {
        return is_liked;
    }

    public void setIsLiked(int isLiked) {
        this.is_liked = isLiked;
    }

    public void setIsLike(Integer isLike) {
        if (isLike != null) {
            int isLiked = isLike.intValue();
            if (isLiked == 0) {
                this.is_disliked = 1;
            } else {
                this.is_liked = 1;
            }
        }
    }
}
