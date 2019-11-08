package com.example.mohamedeldimardash.myguardiannewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STATE_NEWS_SECTION = "section";
    private static final String STATE_MENU_ID = "menuId";
    private static final String STATE_MENU_SELECTED_POSITION = "menuPosition";

    private String newsSection;
    private int menuId;
    private int selectedPosition;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            menuId = R.id.nav_home;
            displaySectionContent();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_MENU_ID, menuId);
        outState.putString(STATE_NEWS_SECTION, newsSection);
        outState.putInt(STATE_MENU_SELECTED_POSITION, selectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        if (inState != null) {
            menuId = inState.getInt(STATE_MENU_ID);
            selectedPosition = inState.getInt(STATE_MENU_SELECTED_POSITION);
            newsSection = inState.getString(STATE_NEWS_SECTION);
            displaySectionContent();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method to inflate menu and add items to the action bar
     * @param menu
     * @return boolean flag
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Method to handle action bar item clicks
     * @param item
     * @return boolean flag
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettingScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to handle individual navigation item clicks
     * @param item
     * @return boolean flag
     */
    @SuppressWarnings({"StatementWithEmptyBody", "JavaDoc"})
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        menuId = item.getItemId();

        displaySectionContent();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method to open appropriate news section based on menu item clicked
     */
    private void displaySectionContent() {
        switch (menuId) {
            case R.id.nav_home:
                newsSection = getString(R.string.menu_home).toLowerCase();
                selectedPosition = 1;
                break;
            case R.id.nav_business:
                newsSection = getString(R.string.menu_business).toLowerCase();
                selectedPosition = 2;
                break;
            case R.id.nav_education:
                newsSection = getString(R.string.menu_education).toLowerCase();
                selectedPosition = 3;
                break;
            case R.id.nav_environment:
                newsSection = getString(R.string.menu_environment).toLowerCase();
                selectedPosition = 4;
                break;
            case R.id.nav_fashion:
                newsSection = getString(R.string.menu_fashion).toLowerCase();
                selectedPosition = 5;
                break;
            case R.id.nav_film:
                newsSection = getString(R.string.menu_film).toLowerCase();
                selectedPosition = 6;
                break;
            case R.id.nav_money:
                newsSection = getString(R.string.menu_money).toLowerCase();
                selectedPosition = 7;
                break;
            case R.id.nav_politics:
                newsSection = getString(R.string.menu_politics).toLowerCase();
                selectedPosition = 8;
                break;
            case R.id.nav_sport:
                newsSection = getString(R.string.menu_sport).toLowerCase();
                selectedPosition = 9;
                break;
            case R.id.nav_technology:
                newsSection = getString(R.string.menu_technology).toLowerCase();
                selectedPosition = 10;
                break;
            case R.id.nav_weather:
                newsSection = getString(R.string.menu_weather).toLowerCase();
                selectedPosition = 11;
                break;
            case R.id.nav_world:
                newsSection = getString(R.string.menu_world).toLowerCase();
                selectedPosition = 12;
                break;
            default:
                newsSection = getString(R.string.menu_home).toLowerCase();
                selectedPosition = 1;
                break;
        }

        NewsFragment mNewsFragment = NewsFragment.newInstance(newsSection, selectedPosition);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_content, mNewsFragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * Method to open Settings Activity
     */
    private void openSettingScreen() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
}

