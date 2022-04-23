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
    private int prevButton = R.id.actionHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                prevButton = bottomNavigationView.getSelectedItemId();
                switch(item.getItemId()){
                    case R.id.actionHome:
                        fragment = new UserFeedFragment();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slidein_from_left,R.anim.slideout_to_right).replace(R.id.frameLayout,fragment).commit();
                        break;
                    case R.id.actionNewPost:
                        fragment = new ComposeFragment();
                        if(prevButton == R.id.actionHome){
                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slidein_from_right,R.anim.slideout_to_left).replace(R.id.frameLayout,fragment).commit();
                        }
                        else{
                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slidein_from_left,R.anim.slideout_to_right).replace(R.id.frameLayout,fragment).commit();
                        }
                        break;
                    case R.id.actionProfile:
                        fragment = new ProfileFragment();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slidein_from_right,R.anim.slideout_to_left).replace(R.id.frameLayout,fragment).commit();
                        break;
                    default:
                        fragment = new UserFeedFragment();
                        fragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit();
                        break;
                }
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