package com.example.jlne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jlne.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class OrganizationActivity extends AppCompatActivity {
    private RecyclerView organizationRV;
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        TextView headerTV = findViewById(R.id.header_TV);
        organizationRV = findViewById(R.id.organization_RV);

        String type = getIntent().getStringExtra("type");
        String name = getIntent().getStringExtra("name");

        headerTV.setText(name);

        if (type != null) {
            if (type.equals("Company")) {
                data = getResources().getStringArray(R.array.company_menu);
            }
            else if (type.equals("Party")){
                data = getResources().getStringArray(R.array.party_menu);
            }
        }

        ArrayList<String> dataList =
                new ArrayList<>(Arrays.asList(data));
        CategoryAdapter categoryAdapter = new CategoryAdapter(dataList);

        organizationRV.setHasFixedSize(true);
        organizationRV.setLayoutManager(new LinearLayoutManager(this));
        organizationRV.setAdapter(categoryAdapter);

        Fragment fragment = new OrganizationSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("organization_name", name);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}