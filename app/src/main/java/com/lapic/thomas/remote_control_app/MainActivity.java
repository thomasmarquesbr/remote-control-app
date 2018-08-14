package com.lapic.thomas.remote_control_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

interface DrawerLocker {
    public void setDrawerEnabled(boolean enabled);
}

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AHBottomNavigation.OnTabSelectedListener, SeekBar.OnSeekBarChangeListener, DrawerLocker {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) protected NavigationView navigationView;
    @BindView(R.id.view_pager) protected AHBottomNavigationViewPager viewPager;
    @BindView(R.id.bottom_navigation) protected AHBottomNavigation bottomNavigation;

    private RCViewPagerAdapter rcViewPagerAdapter;
    private RCViewFragment currentFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SeekBar seekBarVolume;
    private SeekBar seekBarPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setConfigDrawer();
        setConfigBottomNavigation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openDialogSetttingIp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setConfigDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setDrawerEnabled(false);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setConfigBottomNavigation() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.main, R.drawable.ic_tv, R.color.shamrockGreen);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.linear_tv, R.drawable.ic_list, R.color.shamrockGreen);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.on_demand, R.drawable.ic_ondemand, R.color.shamrockGreen);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.input, R.drawable.ic_mouse,R.color.shamrockGreen);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

//        bottomNavigation.setForceTitlesHide(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentFragment == null) {
                    currentFragment = rcViewPagerAdapter.getCurrentFragment();
                }
                viewPager.setCurrentItem(position,false);
                currentFragment = rcViewPagerAdapter.getCurrentFragment();

                if (position == 2) {
                    seekBarVolume = (SeekBar) currentFragment.getView().findViewById(R.id.seekBar_volume);
                    seekBarVolume.setOnSeekBarChangeListener(MainActivity.this);
                    seekBarPlayer = (SeekBar) currentFragment.getView().findViewById(R.id.seekBar_player);
                    seekBarPlayer.setOnSeekBarChangeListener(MainActivity.this);
                }

                return true;
            }
        });

        viewPager.setOffscreenPageLimit(4);
        rcViewPagerAdapter = new RCViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(rcViewPagerAdapter);
        currentFragment = rcViewPagerAdapter.getCurrentFragment();
    }

    @SuppressLint("ResourceType")
    public static String getId(View view) {
        if (view.getId() == 0xffffffff) return "no-id";
        else {
            String completeId = view.getResources().getResourceName(view.getId());
            return completeId.substring(completeId.lastIndexOf("/") + 1);
        }
    }

    public void openDialogSetttingIp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.addTextChangedListener(MaskEditUtil.mask(edittext, "###.###.###.###:####"));
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage(R.string.settings_message);
        alert.setTitle(R.string.action_settings);
        alert.setView(edittext);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String textValue = edittext.getText().toString();
            }
        });
        alert.setNegativeButton(R.string.cancel, null);
        alert.show();
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (currentFragment == null) {
            currentFragment = rcViewPagerAdapter.getCurrentFragment();
        }
        viewPager.setCurrentItem(position,false);
        currentFragment = rcViewPagerAdapter.getCurrentFragment();
        return true;
    }

    public void onClickButton(View view) {
        Toast.makeText(this, "Clicou em " + getId(view), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(this, "Clicou em " + getId(seekBar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }
}
