package com.example.jlne;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jlne.fragment.AddMachineFragment;
import com.example.jlne.fragment.AddOrganizationFragment;
import com.example.jlne.fragment.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final String TAG = "__Main";
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up Toolbar
        Toolbar mainTB = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainTB);

        // Setting up Drawer Layout
        drawer = findViewById(R.id.main_drawer_layout);
        NavigationView navigation = findViewById(R.id.navigation);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                mainTB,
                R.string.drawer_open,
                R.string.drawer_close
        );

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigation.setNavigationItemSelectedListener(this);

        // Setting up home fragment
        Fragment fragment = new HomeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .add(fragment, "__Home");
        fragmentTransaction.replace(R.id.main_host_FL, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            Log.d(TAG, "Nothing To Pop!");
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_machines) {
            // Setting up home fragment
            AddMachineFragment addMachineFragment = AddMachineFragment.newInstance();
            addMachineFragment.show(getSupportFragmentManager(), "__AddMachine");

            /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .add(addMachineFragment, "__AddMachine")
                    .addToBackStack(null);
            fragmentTransaction.replace(R.id.main_host_FL, addMachineFragment);
            fragmentTransaction.commit();*/
        } else if (id == R.id.add_organizations) {
            AddOrganizationFragment addOrganizationFragment = AddOrganizationFragment.newInstance();
            addOrganizationFragment.show(getSupportFragmentManager(), "__AddOrganization");
        }

        drawer.closeDrawers();
        return true;
    }
}
