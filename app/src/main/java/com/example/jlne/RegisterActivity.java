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

public class RegisterActivity extends AppCompatActivity {
    final static String TAG = "__Register";
    private EditText usernameET, passwordET;
    private ProgressBar registerPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        registerPB = findViewById(R.id.register_PB);

        // Check if user is already logged in
        PreferenceHelper preferenceHelper = new PreferenceHelper(RegisterActivity.this);
        if (preferenceHelper.getInstance().isLoggedIn()) {
            finish();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
    }

    public void register(View view) {
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

        UserRegister userRegister = new UserRegister(username, password, currentTime);
        userRegister.execute();
    }

    public class UserRegister extends AsyncTask<Void, Void, String> {
        private String username, password, currentTime;

        public UserRegister (String username, String password, String currentTime) {
            this.username = username;
            this.password = password;
            this.currentTime = currentTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            parameters.put("password", password);
            parameters.put("create_date", currentTime);

            RequestHelper requestHelper = new RequestHelper();
            return requestHelper.sendPostRequest(URLS.REGISTER_URL, parameters);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            registerPB.setVisibility(View.GONE);
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
                            new PreferenceHelper(RegisterActivity.this);
                    preferenceHelper.getInstance().setUserData(user);

                    finish();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            } catch (JSONException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public void goLogin(View view)  {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}