package cn.edu.zju.isst.entity;

public class Show {
    private int id;
    
    private int year;
    
    private String name;
    
    private String organization;
    
    private String category;
    
    private int status;
    
    private int sort_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSortNum() {
        return sort_num;
    }

    public void setSortNum(int sortNum) {
        this.sort_num = sortNum;
    }
}
