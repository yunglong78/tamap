package com.uppc.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.uppc.models.UserBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by songlinwei on 16/4/22.
 */
public class LoginUtil {
    private boolean isLogged = false;
    private PropertyUtil store;
    private UserBean user;

    private static LoginUtil instance;

    public static LoginUtil getInstance(Context c) {
        if (instance == null) {
            instance = getSync(c);
        }
        return instance;
    }

    /**
     * 获取登录信息
     */
    public UserBean getLoggedUser() {
        return user;
    }

    /**
     * 初始化用户登录信息
     */
    private UserBean loadLoggedUser() {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            user = gson.fromJson(store.loadString("user"), UserBean.class);
        } catch (JsonSyntaxException e) {
            //do nothing
        }
        return user;
    }

    private static synchronized LoginUtil getSync(Context c) {
        if(instance == null) {
            instance = new LoginUtil(c);
        }
        return instance;
    }

    private LoginUtil(Context c){
        store = new PropertyUtil(c.getApplicationContext());
        loadLoggedUser();
    }
}
