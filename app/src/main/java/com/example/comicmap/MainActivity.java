package com.example.comicmap;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment_map, fragment_favorite, fragment_search, fragment_checklist, fragment_route;
    private boolean isOpen = false;
    private DrawerLayout drawerLayout;
    private NavigationView drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header_view = drawerView.getHeaderView(0);
        LinearLayout header = (LinearLayout) nav_header_view.findViewById(R.id.drawer_item);
        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();

        //Set Layout Button in NavigationView.. Fuck..
        LinearLayout map_button = (LinearLayout) header.findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("exploit", "map_button_clicked!");
                fragment_map = new fragment_map();
                viewMenu(fragment_map);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //favorite Button Layer
        LinearLayout favorite_button = (LinearLayout) header.findViewById(R.id.favorite_button);
        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("exploit", "favorite_button_clicked!");
                fragment_favorite = new fragment_favorite();
                viewMenu(fragment_favorite);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //Search Button Layer
        LinearLayout search_button = (LinearLayout) header.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("exploit", "search_button_clicked!");
                fragment_search = new fragment_search();
                viewMenu(fragment_search);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //Trade Function has deprecated..

        LinearLayout checklist_button = (LinearLayout) header.findViewById(R.id.trade_button);
        checklist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("exploit", "trade_button_clicked!");
                fragment_checklist = new fragment_checklist();
                viewMenu(fragment_checklist);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        //Route Button Layer
        LinearLayout route_button = (LinearLayout) header.findViewById(R.id.route_button);
        route_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("exploit", "route_button_clicked!");
                fragment_route = new fragment_route();
                viewMenu(fragment_route);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }
    private void viewMenu(Fragment selected_frag) {
        FragmentManager manager = getSupportFragmentManager();
        String fragmentTag = selected_frag.getClass().getSimpleName();
        Log.e("exploit", "Tag name : " + fragmentTag);
        manager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                .add(R.id.fragment, selected_frag)
                .addToBackStack(fragmentTag)
                .commit();
        /*
        if(!isOpen) {

            int x = layoutFragment.getRight();
            int y = layoutFragment.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutFragment.getWidth(), layoutFragment.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutFragment, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

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
        */
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.e("exploit", "BackButton Pressed!");
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