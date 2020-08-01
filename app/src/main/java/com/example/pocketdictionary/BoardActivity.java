package com.example.pocketdictionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

public class BoardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        //Setup the hamburger icon and rotation in toolbar
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        //Setup Navigation Drawer and listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.nav_dictionary:
                        //Display Dictionary Fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDictionary()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_favorite:
                        //Display Dictionary Fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentFavorites()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_feedback:
                        //Display Dictionary Fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentFeedback()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_share:
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"Pocket Dictionary\nA project of Komal, Charanjit and Angel\nCopyright 2020.");
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent,"Share using"));
                }

                //Close drawer after click
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //Display initial fragment
        if(savedInstanceState == null) {
                //Display Dictionary Fragment as initial fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDictionary()).commit();

                //Set check state on Navigation Drawer to Dictionary
                navigationView.setCheckedItem(R.id.nav_dictionary);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }


}
