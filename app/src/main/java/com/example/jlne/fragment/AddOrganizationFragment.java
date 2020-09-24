package com.example.jlne.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jlne.R;
import com.example.jlne.helper.RequestHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddOrganizationFragment extends DialogFragment {
    final String TAG = "__AddOrganization";
    private AutoCompleteTextView typeET;
    private TextInputEditText nameET, addressET, contactET, remarksET;
    private ProgressBar addPB;
    private MaterialButton addBT;

    public AddOrganizationFragment() {
        // Required empty public constructor
    }

    public static AddOrganizationFragment newInstance() {
        return new AddOrganizationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_orgaznization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameET = view.findViewById(R.id.name_ET);
        addressET = view.findViewById(R.id.address_ET);
        typeET = view.findViewById(R.id.type_ET);
        contactET = view.findViewById(R.id.contact_ET);
        remarksET = view.findViewById(R.id.remarks_ET);
        addPB = view.findViewById(R.id.add_PB);
        addBT = view.findViewById(R.id.add_BT);

        // Setting up typeET dropdown menu
        ArrayList<String> types = new ArrayList<>();
        types.add("Company");
        types.add("Party");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, types);
        typeET.setAdapter(typeAdapter);

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrganization();
            }
        });
    }

    public void addOrganization() {
        String name = nameET.getText().toString().trim();
        String address = addressET.getText().toString().trim();
        String contact = contactET.getText().toString().trim();
        String remarks = remarksET.getText().toString().trim();
        String type = typeET.getText().toString().trim();
        String create_date =
                java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        if (name.isEmpty()) {
            nameET.setError("Name must not be empty!");
            nameET.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            addressET.setError("Address must not be empty!");
            typeET.setError("Type must not be empty!");
            addressET.requestFocus();
        }

        if (typeET.getText().toString().equals("")) {
            typeET.setError("Type must not be empty!");
            typeET.requestFocus();
        }

        AddOrganization addOrganization = new AddOrganization(name, address, type, contact, remarks, create_date);
        addOrganization.execute();
    }

    public class AddOrganization extends AsyncTask<Void, Void, String> {
        private String name, address, contact, remarks, type, create_date;

        public AddOrganization(String name, String address, String type, String contact, String remarks, String create_date) {
            this.name = name;
            this.address = address;
            this.type = type;
            this.contact = contact;
            this.remarks = remarks;
            this.create_date = create_date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("address", address);
            parameters.put("type", type);
            parameters.put("contact", contact);
            parameters.put("remarks", remarks);
            parameters.put("create_date", create_date);

            RequestHelper requestHelper = new RequestHelper();
            return requestHelper.sendPostRequest(URLS.ADD_ORGANIZATION_URL, parameters);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addPB.setVisibility(View.GONE);
            try {
                JSONObject responseObject = new JSONObject(s);

                if (!responseObject.getBoolean("error")) {
                    // If category fragment is showing refresh it
                    Fragment fragment = getParentFragmentManager().findFragmentByTag("__Category");
                    if (fragment != null && fragment.isVisible()) {
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.detach(fragment).attach(fragment).commit();
                        Log.d(TAG, "Category Fragment refreshed!");
                    }

                    Toast.makeText(getActivity(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }

                else {
                    Toast.makeText(getActivity(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }
}