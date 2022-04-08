package com.example.chattingroom;

public class Room {

    private int roomId;
    private boolean isPrivate;
    private String password;
    private String name;
    private String discription;

    public Room() {}

    public Room(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public Room(int roomId, boolean isPrivate, String password, String name, String discription) {
        this.roomId = roomId;
        this.isPrivate = isPrivate;
        this.password = password;
        this.name = name;
        this.discription = discription;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
