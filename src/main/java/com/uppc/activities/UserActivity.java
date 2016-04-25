package com.uppc.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import com.uppc.R;
import com.uppc.fragments.LoginFragment;
import com.uppc.models.UserBean;

public class UserActivity extends FragmentActivity implements LoginFragment.OnLoggedInListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onLoggedIn(UserBean user) {

    }
}
