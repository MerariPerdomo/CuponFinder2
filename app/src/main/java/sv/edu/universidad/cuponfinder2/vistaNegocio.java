package sv.edu.universidad.cuponfinder2;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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

import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class vistaNegocio extends AppCompatActivity {
    private TextView negocio;
    private ImageView portada, perfil;
    private StorageReference storageReference;
    private Button regresar;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private PromocionesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_negocio);
        negocio=findViewById(R.id.Username);
        portada=findViewById(R.id.imgPortada);
        perfil=findViewById(R.id.img_perfil_negocio);
        regresar=findViewById(R.id.btnRegresar);
        regresar.setOnClickListener(v -> onBackPressed());
        storageReference = FirebaseStorage.getInstance().getReference();
        recyclerView = findViewById(R.id.rvPromoNegocio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference("Promociones");


        String id = getIntent().getStringExtra("id");
        MostrarPromos(id);
        ObtenerUsuario(id);

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
                    adapter = new PromocionesAdapter(promocions);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ObtenerUsuario(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Negocio usuarios = dataSnapshot.getValue(Negocio.class);
                negocio.setText(usuarios.getNegocio());
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
                Picasso.get().load(imageUrl).into(perfil);
            }
        });
        StorageReference reference2 = storageReference.child("fondo/*" + id);
        reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Foto de portada
                String imageUrl = uri.toString();
                Picasso.get().load(imageUrl).into(portada);
            }
        });
    }
}