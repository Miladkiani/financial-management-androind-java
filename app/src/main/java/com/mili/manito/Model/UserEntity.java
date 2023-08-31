package com.mili.manito.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users",
indices = {@Index(value = "user_name",unique = true)} )
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private int user_id;


    private String user_name;


    private String user_pass;

    public UserEntity(int user_id,  String user_name,  String user_pass) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_pass = user_pass;
    }

    @Ignore
    public UserEntity(String user_name, String user_pass) {
        this.user_name = user_name;
        this.user_pass = user_pass;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }
}
