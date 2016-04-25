package com.uppc.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by songlinwei on 16/4/22.
 */
public class UserBean {
    private int id;
    private String username;
    private String password;
    private String mobile;
    private String fullname;
    private String company;
    private String avatar;
    private Date created;
    private Date updated;

    public static UserBean fromJSONObject(JSONObject json) {
        UserBean user = new UserBean();
        try {
            user.setId(json.getInt("id"));
            user.setUsername(json.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
