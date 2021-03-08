package com.gujja.ajay.brucew.model;

public class Repo {

    int id;
    String login;
    String avatar;
    String type;

    public Repo(int id, String login, String avatar, String type) {
        this.id = id;
        this.login = login;
        this.avatar = avatar;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
