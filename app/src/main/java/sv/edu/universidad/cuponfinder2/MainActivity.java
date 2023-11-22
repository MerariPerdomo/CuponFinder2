package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Adaptor.CategoryAdaptor;
import sv.edu.universidad.cuponfinder2.Adaptor.PromosAdaptor;
import sv.edu.universidad.cuponfinder2.Model.Promocion;
import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.domain.CategoryDomain;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CategoryAdaptor.OnItemClickListener, SearchView.OnQueryTextListener {

    /*-------------Validacion de conexion-------------*/
    private noConexion noConnectionFragment;

    /*-------------Seccion de Categorias y promociones-------------*/

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListCategorias, recyclerViewListPromo;
    private PromocionesAdapter adapterPromocion;
    private final List<Promocion> promocions = new ArrayList<>();
    private SearchView searchView;




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
        searchView = findViewById(R.id.scvPromotion);
        noConnectionFragment = new noConexion();
        recyclerViewListPromo=findViewById(R.id.rvPromotiones);
        recyclerViewListPromo.setLayoutManager(new LinearLayoutManager(this));



        /*--------------Para las cards de promocion ---------------*/
        if(!isConnectedToInternet()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.noConnectionContainer, noConnectionFragment)// Oculta el fragmento al inicio
                    .commit();
        }else{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Promociones");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Promocion> oldPromociones = new ArrayList<>(promocions);
                    promocions.clear();
                    Date currentDate = new Date();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
                    String dateToday = sdf.format(currentDate);
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Promocion promotion = dataSnapshot1.getValue(Promocion.class);
                        assert promotion != null;
                        if (promotion.getFechaInicio().compareTo(dateToday) <= 0 && promotion.getFechaFinal().compareTo(dateToday) >= 0) {
                            promocions.add(promotion);
                        }
                    }
                    if (adapterPromocion == null) {
                        adapterPromocion = new PromocionesAdapter(promocions);
                        recyclerViewListPromo.setAdapter(adapterPromocion);
                    } else {
                        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyCallback(oldPromociones, promocions));
                        result.dispatchUpdatesTo(adapterPromocion);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ERROR", databaseError.getMessage());
                }
            });
            searchView.setOnQueryTextListener(this);
        }


        /*--------------Para las cards de promocion ---------------*/
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
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Negocio usuarios = dataSnapshot.getValue(Negocio.class);
                        assert usuarios != null;
                        nombreUsuario.setText(usuarios.getNombre());
                    }else{
                        nombreUsuario.setText("No hay usuarios en la base");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar error
                }
            });
            StorageReference reference = storageReference.child("perfil/*" + id);
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                //Foto de perfil
                String imageUrl = uri.toString();
                Picasso.get().load(imageUrl).into(perfil_foto);
            });
        }else{
            nombreUsuario.setText("No Register");
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

        adapter=new CategoryAdaptor(categoria, this);
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
            if (mAuth.getCurrentUser() != null){
                Intent intent = new Intent(getApplicationContext(), vistaUsurio.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
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
    @Override
    public void onItemClick(String title) {
        Intent intent = new Intent(this, promotions.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void search(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Promociones");
        Query query = ref.orderByChild("titulo").startAt(s.toLowerCase()).endAt(s.toLowerCase()+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promocions.clear();
                Date currentDate = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
                String dateToday = sdf.format(currentDate);
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Promocion promotion = dataSnapshot1.getValue(Promocion.class);
                    assert promotion != null;
                    if (promotion.getTitulo().toLowerCase().contains(s.toLowerCase()) && promotion.getFechaInicio().compareTo(dateToday) <= 0 && promotion.getFechaFinal().compareTo(dateToday) >= 0) {
                        promocions.add(promotion);
                    }
                }
                if (adapterPromocion == null) {
                    adapterPromocion = new PromocionesAdapter(promocions);
                    recyclerViewListPromo.setAdapter(adapterPromocion);
                } else {
                    adapterPromocion.setPromocions(promocions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}


