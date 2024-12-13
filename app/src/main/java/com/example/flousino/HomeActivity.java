package com.example.flousino;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
// Fragement
    private DashboardFragment dashboardFragment;
    private AjouterFragment ajouterFragment;
    private ExpenseFragment expenseFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Configuration de la barre d'outils
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Configuration du tiroir de navigation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       NavigationView navigationView = findViewById(R.id.naView);
       navigationView.setNavigationItemSelectedListener(this);

       dashboardFragment = new DashboardFragment();
       ajouterFragment = new AjouterFragment();
       expenseFragment = new ExpenseFragment();

       bottomNavigationView = findViewById(R.id.bottom_navigation);
       frameLayout = findViewById(R.id.main_frame);
       setFragment(dashboardFragment);
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.dashboard)
                {
                    setFragment(dashboardFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                }
                else if (itemId == R.id.ajouter)
                {
                    setFragment(ajouterFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.ajouter_color);

                }
                else if(R.id.expenses==itemId)
                {
                    setFragment(expenseFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.exepenses_color);
                }

                else{
                    return false;
                }
                return true;
               }
            });
        // Gestion du bouton retour
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) { // Utilisation correcte de START
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        String message = "";

        if (itemId == R.id.dashboard) {
            setFragment(dashboardFragment);
            bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
        }
        else if (itemId == R.id.ajouter) {
            setFragment(ajouterFragment);
            bottomNavigationView.setItemBackgroundResource(R.color.ajouter_color);
        }
        else if(R.id.expenses==itemId)
        {
            setFragment(expenseFragment);
            bottomNavigationView.setItemBackgroundResource(R.color.exepenses_color);
        }
        else{
            message = "Unknown menu item!";
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
