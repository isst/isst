package cn.edu.zju.isst.entity;

public class LoggedUser {
    private int id = 0;
    private String name;
    private String fullname;
    private String password;
    private int type = 0;
    private String nickname;

    public LoggedUser() {
        
    }
    
    public LoggedUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.fullname = user.getFullname();
        this.password = user.getPassword();
        this.type = user.getType();
        this.nickname = user.getNickname();
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }

    public String getNickname() {
        return nickname;
    }

}
