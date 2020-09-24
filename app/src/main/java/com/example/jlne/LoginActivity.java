package com.example.jlne;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.jlne.helper.PreferenceHelper;
import com.example.jlne.helper.RequestHelper;
import com.example.jlne.helper.URLS;
import com.example.jlne.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    final static String TAG = "__Login";
    private EditText usernameET, passwordET;
    private ProgressBar loginPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        loginPB = findViewById(R.id.login_PB);

        // Check if user is already logged in
        PreferenceHelper preferenceHelper = new PreferenceHelper(LoginActivity.this);
        if (preferenceHelper.getInstance().isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void login(View view) {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        if (username.isEmpty()) {
            usernameET.setError("Username must not be empty!");
            usernameET.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordET.setError("Password must not be empty!");
            passwordET.requestFocus();
        }

        UserLogin userLogin = new UserLogin(username, password, currentTime);
        userLogin.execute();
    }

    public class UserLogin extends AsyncTask<Void, Void, String> {
        private String username, password, currentTime;

        public UserLogin (String username, String password, String currentTime) {
            this.username = username;
            this.password = password;
            this.currentTime = currentTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            parameters.put("password", password);
            parameters.put("current_time", currentTime);

            RequestHelper requestHelper = new RequestHelper();
            return requestHelper.sendPostRequest(URLS.LOGIN_URL, parameters);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loginPB.setVisibility(View.GONE);
            Log.d(TAG, s);

            try {
                JSONObject responseObject = new JSONObject(s);

                if (!responseObject.getBoolean("error")) {
                    JSONObject userObject = responseObject.getJSONObject("user");
                    User user = new User(
                            userObject.getInt("id"),
                            userObject.getString("username"),
                            userObject.getString("create_date"),
                            userObject.getString("last_login")
                    );

                    PreferenceHelper preferenceHelper =
                            new PreferenceHelper(LoginActivity.this);
                    preferenceHelper.getInstance().setUserData(user);

                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            } catch (JSONException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public void goRegister(View view)  {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}