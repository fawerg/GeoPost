package com.merati.project.geopost;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ActivityFollowed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    protected String session_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed);

        Intent intent = getIntent();
        session_id = intent.getStringExtra("session");

        setNavigationViewListner();
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.menulaterale);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.profile:
                intent =new Intent(this, ProfileActivity.class);
                break;
            case R.id.add_friend:
                break;
            case R.id.followed:
                DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;

            case R.id.status_update:
                break;
        }
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        intent.putExtra("session_id", session_id);
        startActivity(intent);
        return true;
    }
}
