package cn.edu.zju.isst.entity;

public class AppRelease {
    public final static int ANDROID = 0;
    
    private int id;
    private int type;
    private int build;
    private String version;
    private String url;
    private long post_time;
    private String readme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPostTime() {
        return post_time;
    }

    public void setPostTime(long postTime) {
        this.post_time = postTime;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
