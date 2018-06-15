package com.dena.sb_druid.entity;

import javax.validation.constraints.Min;

public class User {

    private Integer id;

    private String userName;

    @Min(value = 18,message = "18禁")
    private Integer age;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
