package com.example.comicmap;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    private Fragment fragment;
    private ConstraintLayout layoutFragment;
    private boolean isOpen = false;
    private DrawerLayout drawerLayout;
    private View drawerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutFragment = findViewById(R.id.fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (View)findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("exploit", "NAV_VIEW_clicked!");
                Fragment fragment = null;
                switch (v.getId()) {
                    case R.id.map_button:
                        Log.e("exploit", "map_button_clicked!");
                        fragment = new fragment_map();
                        break;
                    case R.id.favorite_button:
                        Log.e("exploit", "favorite_button_clicked!");
                        fragment = new fragment_favorite();
                        break;
                    case R.id.search_button:
                        Log.e("exploit", "search_button_clicked!");
                        fragment = new fragment_favorite();
                        break;
                    case R.id.trade_button:
                        Log.e("exploit", "trade_button_clicked!");
                        fragment = new fragment_trade();
                        break;
                    case R.id.route_button:
                        Log.e("exploit", "route_button_clicked!");
                        fragment = new fragment_route();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                if (fragment != null) {
                    viewMenu(fragment);
                }
                return true;
            }
            });
    }

    private void viewMenu(Fragment selected_frag) {
        FragmentManager manager = getSupportFragmentManager();
        fragment = selected_frag;
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

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