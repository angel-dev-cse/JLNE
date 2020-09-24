package com.example.jlne;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jlne.adapter.MachineCountAdapter;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.textview.MaterialTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;

public class OrganizationSummaryFragment extends Fragment {
    final String TAG = "JLNE_Org_Summary";
    private RecyclerView machineCountRV;
    private String name;

    public static OrganizationSummaryFragment newInstance() {
        return new OrganizationSummaryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organization_summary_fragment, container, false);
        machineCountRV = view.findViewById(R.id.machine_count_RV);

        if (getArguments() != null) {
            name = getArguments().getString("organization_name");
        }

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("organization_name", name);

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_ORGANIZATION_MACHINE_SUMMARY, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                Log.d(TAG, result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray machineNameArray = jsonObject.getJSONArray("machine_names");
                    JSONArray machineCountArray = jsonObject.getJSONArray("machine_count");

                    HashMap<String, Integer> machineSummaryData = new HashMap<>();

                    if (machineNameArray.length() == 0) {
                        machineSummaryData.put("Machine Available", 0);
                    }
                    else {
                        for (int i = 0; i < machineNameArray.length(); i++) {
                            machineSummaryData.put(machineNameArray.getString(i), machineCountArray.getInt(i));
                        }
                    }

                    MachineCountAdapter machineCountAdapter = new MachineCountAdapter(machineSummaryData);
                    machineCountRV.setAdapter(machineCountAdapter);
                    machineCountRV.setHasFixedSize(true);
                    machineCountRV.setLayoutManager(new LinearLayoutManager(getActivity()));

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();

        return view;
    }
}