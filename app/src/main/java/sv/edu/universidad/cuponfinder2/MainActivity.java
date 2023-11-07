package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import sv.edu.universidad.cuponfinder2.Adaptor.CategoryAdaptor;
import sv.edu.universidad.cuponfinder2.domain.CategoryDomain;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*-------------Seccion de Categorias -------------*/

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListCategorias;




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

        /*-------------Seccion de Categorias -------------*/
        recyclerViewCategorias();


    }



    private void recyclerViewCategorias() {
        LinearLayoutManager car = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListCategorias=findViewById(R.id.rvCategories);
        recyclerViewListCategorias.setLayoutManager(car);

        ArrayList<CategoryDomain> categoria = new ArrayList<>();
        categoria.add(new CategoryDomain("Restaurant","C1"));
        categoria.add(new CategoryDomain("Market","C2"));
        categoria.add(new CategoryDomain("Health","C3"));
        categoria.add(new CategoryDomain("Pets","C4"));
        categoria.add(new CategoryDomain("Drinks","C5"));
        categoria.add(new CategoryDomain("Coffee Shops","C6"));

        adapter=new CategoryAdaptor(categoria);
        recyclerViewListCategorias.setAdapter(adapter);

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
//            Intent intent = new Intent(MainActivity.this, Editar_promocion.class);
//            startActivity(intent);
            Toast.makeText(this, "Te lleva a home", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, vistaUsurio.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_acerca) {
            Intent intent = new Intent(MainActivity.this, AcercaDe.class);
            startActivity(intent);
            Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, Configuracion.class);
            startActivity(intent);
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