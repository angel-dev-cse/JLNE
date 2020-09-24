package com.example.jlne.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.jlne.LoginActivity;
import com.example.jlne.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PreferenceHelper {
    private final String SHARED_PREF_NAME = "JLNE_shared_pref";
    private final String CATEGORY_PREF_NAME = "JLNE_category_pref";
    private final String ID_KEY = "id";
    private final String USERNAME_KEY = "username";
    private final String CREATED_KEY = "create_date";
    private final String LAST_LOGIN_KEY = "last_login";
    private final String LOGGED_STATUS_KEY = "logged_status";
    private final Context context;
    private PreferenceHelper preferenceHelperInstance;

    public PreferenceHelper(Context context) {
        this.context = context;
    }

    public synchronized PreferenceHelper getInstance() {
        if (this.preferenceHelperInstance == null) {
            preferenceHelperInstance = new PreferenceHelper(context);
        }

        return preferenceHelperInstance;
    }

    public void setUserData(User user) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID_KEY, user.getId());
        editor.putString(USERNAME_KEY, user.getUsername());
        editor.putString(CREATED_KEY, user.getCreate_date());
        editor.putString(LAST_LOGIN_KEY, user.getLast_login());
        editor.putBoolean(LOGGED_STATUS_KEY, true);
        editor.apply();
    }

    public void setCategoryPreference(HashMap<String, Set<String>> categoryData) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CATEGORY_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, Set<String>> category : categoryData.entrySet()) {
            editor.putStringSet(category.getKey(), category.getValue());
        }

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGGED_STATUS_KEY, false);
    }

    public User getUser() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(ID_KEY, -1),
                sharedPreferences.getString(USERNAME_KEY, null),
                sharedPreferences.getString(CREATED_KEY, null),
                sharedPreferences.getString(LAST_LOGIN_KEY, null)
        );
    }

    public void logOut() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
