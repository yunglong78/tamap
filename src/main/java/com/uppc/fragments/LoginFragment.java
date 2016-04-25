package com.uppc.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.uppc.AppContext;
import org.json.JSONException;
import org.json.JSONObject;
import com.uppc.R;
import com.uppc.models.UserBean;


public class LoginFragment extends Fragment {
    TextView loginBtn; //FIXME

    private OnLoggedInListener mListener;


    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBtn = (TextView) getActivity().findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                login("test", "111111");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoggedInListener) {
            mListener = (OnLoggedInListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoggedInListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void login(String username, String password) {
        String url = "http://touchair.dang5.com/api_v1.0/user";
        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
        } catch (JSONException e) {
            // FIXME
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserBean user = UserBean.fromJSONObject(response);
                mListener.onLoggedIn(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        getAppContext().addToRequestQueue(request);
    }

    public interface OnLoggedInListener {
        void onLoggedIn(UserBean user);
    }

    public AppContext getAppContext()
    {
        return (AppContext) getActivity().getApplicationContext();
    }
}
