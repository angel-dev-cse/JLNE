package com.example.jlne.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jlne.OrganizationSummaryFragment;
import com.example.jlne.R;
import com.example.jlne.adapter.CategoryAdapter;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;

public class OrganizationFragment extends Fragment implements CategoryAdapter.ItemClickListener{
    private RecyclerView organizationRV;
    private MaterialTextView organizationNameTV;
    private String[] data;
    private String name, type;

    public OrganizationFragment() {
        // Required empty public constructor
    }

    public static OrganizationFragment newInstance(String name, String type) {
        OrganizationFragment organizationFragment = new OrganizationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("type", type);
        organizationFragment.setArguments(bundle);

        return organizationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        organizationNameTV = getActivity().findViewById(R.id.organization_name_TV);

        if (getActivity() != null) {
            organizationRV = getActivity().findViewById(R.id.organization_RV);
        }

        organizationNameTV.setText(name);

        if (type != null) {
            if (type.equals("Company")) {
                data = getResources().getStringArray(R.array.company_menu);
            }
            else if (type.equals("Party")){
                data = getResources().getStringArray(R.array.party_menu);
            }
        }

        // Setup RecyclerView
        ArrayList<String> dataList =
                new ArrayList<>(Arrays.asList(data));
        CategoryAdapter categoryAdapter = new CategoryAdapter(dataList);
        categoryAdapter.setItemClickListener(this);

        organizationRV.setHasFixedSize(true);
        organizationRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        organizationRV.setAdapter(categoryAdapter);


        // Setup Summary for Organization Machine
        Fragment fragment = new OrganizationSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("organization_name", name);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(View view, int position) {
        MachineListFragment machineListFragment = MachineListFragment.newInstance(name, type);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                .add(machineListFragment, "__MachineList")
                .addToBackStack(null);

        transaction.replace(R.id.main_host_FL, machineListFragment).commit();
    }
}