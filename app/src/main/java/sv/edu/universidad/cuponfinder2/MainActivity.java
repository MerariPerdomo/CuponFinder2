package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*--------------Para menu lateral ---------------*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*--------------Para menu lateral ---------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /*-----------------fin----------------*/

    }
    /*--------------Para menu lateral ---------------*/
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            drawerLayout.closeDrawers();
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            Toast.makeText(this, "Te lleva a profile", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_acerca) {
            Toast.makeText(this, "Te lleva a about it", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_setting) {
            Toast.makeText(this, "Te lleva a setting", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_exit) {
            finish();
            return true;
        }else {return false;}
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    /*----------------Fin----------------*/
}