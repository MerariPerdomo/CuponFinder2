package sv.edu.universidad.cuponfinder2;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

import sv.edu.universidad.cuponfinder2.Adaptor.MypromoAdaptor;
import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class vistaUsurio extends AppCompatActivity {
    private TextView nombreUsuario, email;
    private ImageView perfilFoto, portadaFoto;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private noConexion noConnectionFragment;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListCategorias, recyclerViewListPromo;
    private List<Promocion> promocions = new ArrayList<>();

    /*Cards*/

    /*Cards*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_usurio);
        nombreUsuario = findViewById(R.id.Username);
        email = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        perfilFoto = findViewById(R.id.img_perfil_negocio);
        portadaFoto = findViewById(R.id.user_portada);
        storageReference = FirebaseStorage.getInstance().getReference();
        recyclerViewListPromo=findViewById(R.id.rvPromosUser);
        recyclerViewListPromo.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference("Promociones");
        noConnectionFragment = new noConexion();


        if(!isConnectedToInternet()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.noConnectionContainer5, noConnectionFragment)// Oculta el fragmento al inicio
                    .commit();
        }else{
            if (mAuth.getCurrentUser() != null) {
                String id = mAuth.getCurrentUser().getUid();
                ObtenerUsuario(id);
                MostrarPromos(id);
            } else {
                nombreUsuario.setText("");
                email.setText("");
            }
        }

    }

    public void Editar(View view) {
        Intent i = new Intent(vistaUsurio.this, editar_usuario.class);
        startActivity(i);
    }

    public void AgregarProm(View view) {
        Intent intent = new Intent(vistaUsurio.this, AgregarPromo.class);
        startActivity(intent);
    }

    public void Regresar(View view) {
        Intent intent = new Intent(vistaUsurio.this, MainActivity.class);
        startActivity(intent);
    }
    public void MostrarPromos(String id){
        mDatabase.orderByChild("idUser").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Promocion> promocions = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Promocion promotion = dataSnapshot1.getValue(Promocion.class);
                    promocions.add(promotion);
                }
                if (adapter == null) {
                    adapter = new MypromoAdaptor(promocions);
                    recyclerViewListPromo.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("DatabaseError", "onCancelled", error.toException());
            }
        });
    }
    public void ObtenerUsuario(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Negocio usuarios = dataSnapshot.getValue(Negocio.class);
                nombreUsuario.setText(usuarios.getNombre());
                email.setText(usuarios.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error
                Log.e("DatabaseError", "onCancelled", databaseError.toException());
            }
        });
        StorageReference reference = storageReference.child("perfil/*" + id);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Foto de perfil
                String imageUrl = uri.toString();
                Picasso.get().load(imageUrl).into(perfilFoto);
            }
        });
        StorageReference reference2 = storageReference.child("fondo/*" + id);
        reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Foto de portada
                String imageUrl = uri.toString();
                Picasso.get().load(imageUrl).into(portadaFoto);
            }
        });
    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
