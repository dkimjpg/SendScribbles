package com.example.sendscribbles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sendscribbles.Fragments.ComposeFragment;
import com.example.sendscribbles.Fragments.ProfileFragment;
import com.example.sendscribbles.Fragments.UserFeedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Sign out
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out the user
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                // go into the login activity page
                goLoginActivity();
                Toast.makeText(MainActivity.this, "Log out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()){
                    case R.id.actionHome:
                        fragment = new UserFeedFragment();
                        break;
                    case R.id.actionNewPost:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.actionProfile:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new UserFeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit();
                return true;
            }

        });

        // set default selection
        bottomNavigationView.setSelectedItemId(R.id.actionHome);

    }

    // User log out function
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}