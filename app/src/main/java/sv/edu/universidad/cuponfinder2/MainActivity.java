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
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sv.edu.universidad.cuponfinder2.Adaptor.CategoryAdaptor;
import sv.edu.universidad.cuponfinder2.Model.Usuarios;
import sv.edu.universidad.cuponfinder2.domain.CategoryDomain;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*-------------Seccion de Categorias -------------*/

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListCategorias, recyclerViewListPromo;




    /*--------------Para menu lateral ---------------*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*--------------Para menu lateral ---------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        View headerView = navigationView.getHeaderView(0);
        ImageView perfil_foto = headerView.findViewById(R.id.perfilMenu);
        TextView nombreUsuario = headerView.findViewById(R.id.nombre_usu);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                    nombreUsuario.setText(usuarios.getNombre());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Manejar error
                }
            });
            StorageReference reference = storageReference.child("perfil/*" + id);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Foto de perfil
                    String imageUrl = uri.toString();
                    Picasso.get().load(imageUrl).into(perfil_foto);
                }
            });
        }else{
            nombreUsuario.setText("Sin registrar");
        }
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), vistaUsurio.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_acerca) {
            Intent intent = new Intent(getApplicationContext(), AcercaDe.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, Configuracion.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_exit) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.nav_negocios) {
            Intent intent = new Intent(MainActivity.this, Negocios.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        } else {return false;}
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    /*----------------Fin----------------*/
}