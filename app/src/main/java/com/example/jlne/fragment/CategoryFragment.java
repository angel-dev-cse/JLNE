package com.example.jlne.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jlne.R;
import com.example.jlne.adapter.CategoryAdapter;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CategoryFragment extends Fragment implements CategoryAdapter.ItemClickListener {

    private final String TAG = "__Category";
    private TextView headerTV;
    private RecyclerView categoryRV;
    private CategoryAdapter categoryAdapter;
    private FloatingActionButton categoryFAB;
    private String organizationType;
    private ArrayList<String> organizationData;


    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(String organizationType) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("organization_type", organizationType);
        categoryFragment.setArguments(bundle);

        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            headerTV = getActivity().findViewById(R.id.header_TV);
            categoryRV = getActivity().findViewById(R.id.category_RV);
            categoryFAB = getActivity().findViewById(R.id.category_FAB);
        }

        if (getArguments() != null) {
            organizationType = getArguments().getString("organization_type");
            headerTV.setText(organizationType);
        }

        categoryFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrganizationFragment addOrganizationFragment = AddOrganizationFragment.newInstance();
                addOrganizationFragment.show(getParentFragmentManager(), "__AddOrganization");
            }
        });

        // Initialize adapter for setting click listener
        categoryAdapter = new CategoryAdapter(null);
        categoryAdapter.setItemClickListener(this);

        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        Fragment organizationFragment = OrganizationFragment
                .newInstance(organizationData.get(position), organizationType);

        if (getActivity() != null) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                    .add(organizationFragment, "__Organization")
                    .addToBackStack(null);
            fragmentTransaction.replace(R.id.main_host_FL, organizationFragment);
            fragmentTransaction.commit();
        }
    }
}