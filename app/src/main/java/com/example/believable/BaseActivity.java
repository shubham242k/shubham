package com.example.believable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        final BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        String to = getIntent().getStringExtra("which activity");



        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naigationView);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        ImageView cancelButton = headerView.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });







        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            bottomNav.setSelectedItemId(R.id.Home);
        }

        try {
            if (to.equals("to cart activity")) {
                getSupportFragmentManager().
                        beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
                bottomNav.setSelectedItemId(R.id.Cart);
            }

        }catch (Exception e){
            Log.e("error",e.toString());
        }

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.Home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.Cart:
                    selectedFragment = new CartFragment();
                    break;
                case R.id.Profile:
                    selectedFragment = new ProfileFragment();
                    break;


            }
            if(selectedFragment!=null){

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();



            }
            else{
                Log.e("error", "Error in creating fragment" );
            }
            return true;
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment gotoFragment = null;
            switch (item.getItemId()){

                case R.id.home:
                    bottomNav.setSelectedItemId(R.id.Home);

                    gotoFragment = new HomeFragment();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.cart:
                    bottomNav.setSelectedItemId(R.id.Cart);
                    gotoFragment = new CartFragment();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.logOut:
                    if (firebaseAuth != null) {
                        firebaseAuth.signOut();
                        Intent i = new Intent(BaseActivity.this, LoginUserActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

            }

            if(gotoFragment!=null){

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,gotoFragment).commit();


            }
            else{
                Log.e("error", "Error in creating fragment" );
            }
            return true;
        });

    }

}