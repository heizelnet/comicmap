package com.example.comicmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager manager;
    private Fragment fragment;
    private ConstraintLayout layoutFragment;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutFragment = findViewById(R.id.fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Comicmap");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void viewMenu() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = new fragment_map();
        if(!isOpen) {

            int x = layoutFragment.getRight();
            int y = layoutFragment.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutFragment.getWidth(), layoutFragment.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutFragment, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    manager.beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
                @Override
                public void onAnimationEnd(Animator animator) { }
                @Override
                public void onAnimationCancel(Animator animator) { }
                @Override
                public void onAnimationRepeat(Animator animator) { }
            });
            anim.start();
            isOpen = true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}

/* SpeedDialView + CircularReveal Backup Code

        //Add Action Items
        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_link, R.drawable.ic_link_white_24dp)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_map, R.drawable.ic_pencil_alt_white_24dp)
                        .create()
        );

        //FAB ActionListener
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_map:
                        viewMenu();
                        return false;
                    case R.id.fab_link:
                        Toast.makeText(getApplicationContext(), "Link clicked!", Toast.LENGTH_SHORT).show();
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });
 */