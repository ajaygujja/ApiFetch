package com.gujja.ajay.brucew.Room;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class API_Data implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "t_loginName")
    String loginName_;

    @ColumnInfo(name = "type___")
    String type___;

    @ColumnInfo(name = "avatar_url")
    String avatar_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName_() {
        return loginName_;
    }

    public void setLoginName_(String loginName_) {
        this.loginName_ = loginName_;
    }

    public String getType___() {
        return type___;
    }

    public void setType___(String type___) {
        this.type___ = type___;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
