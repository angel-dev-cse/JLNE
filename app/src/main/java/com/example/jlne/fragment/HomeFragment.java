package com.example.jlne.fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jlne.LoginActivity;
import com.example.jlne.R;
import com.example.jlne.adapter.CategoryAdapter;
import com.example.jlne.helper.PreferenceHelper;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment implements CategoryAdapter.ItemClickListener{

    private final String TAG = "__Home";
    private RecyclerView categoryRV;
    private BaseViewModel mViewModel;
    private ArrayList<String> categoryList;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect users if not logged in
        PreferenceHelper preferenceHelper = new PreferenceHelper(getActivity());
        if (!preferenceHelper.getInstance().isLoggedIn()) {
            if (getActivity() != null) getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(BaseViewModel.class);

        if (getActivity() != null) {
            categoryRV = getActivity().findViewById(R.id.category_RV);
        }

        categoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_categories)));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.setItemClickListener(this);

        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRV.setAdapter(categoryAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {

        String selectedCategory = categoryList.get(position);
        OrganizationListFragment organizationListFragment;

        if (selectedCategory.equals("Companies")) {
            organizationListFragment = OrganizationListFragment.newInstance("Company");
        }

        else organizationListFragment = OrganizationListFragment.newInstance("Party");

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                .add(organizationListFragment, "__OrganizationList")
                .addToBackStack(null);
        transaction.replace(R.id.main_host_FL, organizationListFragment);
        transaction.commit();
    }

}