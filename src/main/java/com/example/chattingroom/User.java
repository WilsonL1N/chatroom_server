package com.example.chattingroom;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int uid;
    private String name;
    private String password;
    
    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(int uid, String name, String password) {
        this.uid = uid;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Users [name=" + name + ", uid=" + uid + "]";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
