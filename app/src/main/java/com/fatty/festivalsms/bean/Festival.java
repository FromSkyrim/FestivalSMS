package com.fatty.festivalsms.bean;

/**
 * Created by 17255 on 2016/6/2.
 */
public class Festival {

    private int id;
    private String name;
    private String desc;

    public Festival(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
