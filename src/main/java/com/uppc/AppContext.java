package com.uppc;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.uppc.models.UserBean;
import com.uppc.utils.LoginUtil;

/**
 * Created by songlinwei on 16/4/22.
 */
public class AppContext extends Application {
    public static final String VOLLEY_TAG = "VolleyPatterns";
    private RequestQueue mRequestQueue;

    public UserBean getLoggedUser()
    {
        return LoginUtil.getInstance(this).getLoggedUser();
    }

    /**
     * The Volley Request queue, the queue will be created if it is null
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? VOLLEY_TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, VOLLEY_TAG);
    }
}
