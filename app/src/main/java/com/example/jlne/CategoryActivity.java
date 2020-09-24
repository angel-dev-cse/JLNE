package com.example.jlne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jlne.adapter.CategoryAdapter;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.PreferenceHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener{
    final String TAG = "JLNE_Category_Act";
    private CategoryAdapter categoryAdapter;
    private ArrayList<String> organizationData;
    private String organizationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        TextView headerTV = findViewById(R.id.header_TV);
        TextView usernameTV = findViewById(R.id.username_TV);
        RecyclerView categoryRV = findViewById(R.id.category_RV);
        FloatingActionButton categoryFAB = findViewById(R.id.category_FAB);

        organizationType = getIntent().getStringExtra("organization_type");
        headerTV.setText(organizationType);

        PreferenceHelper preferenceHelper = new PreferenceHelper(CategoryActivity.this);
        if (preferenceHelper.getInstance().isLoggedIn()) {
            usernameTV.setText(preferenceHelper.getInstance().getUser().getUsername());
        }

        // Initialize adapter for setting click listener
        categoryAdapter = new CategoryAdapter(null);
        categoryAdapter.setItemClickListener(this);

        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(this));
        categoryRV.setAdapter(categoryAdapter);

        if (organizationType != null) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("type", organizationType.toLowerCase());

            AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_ORGANIZATION_URL, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        // Data is received here
                        Log.d(TAG, result);

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray organizationNames = jsonObject.getJSONArray("organization_names");

                        ArrayList<String> data = new ArrayList<>();
                        for (int i = 0; i < organizationNames.length(); i++) {
                            data.add(organizationNames.get(i).toString());
                        }

                        organizationData = data; //pass the data to local for item click position
                        categoryAdapter.updateData(data);

                    } catch (JSONException e) {
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    }
                }
            }).execute();
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(CategoryActivity.this, OrganizationActivity.class);
        intent.putExtra("name", organizationData.get(position));
        intent.putExtra("type", organizationType);
        startActivity(intent);
    }
}