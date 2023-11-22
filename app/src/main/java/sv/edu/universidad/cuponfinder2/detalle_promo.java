package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class detalle_promo extends AppCompatActivity {
    TextView nombrePromo, nombreLocal, categoria, descripcion, fechaInicio, fechaFin;
    ImageView imgDetallePromo;
    Button btnRHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_promo);

        imgDetallePromo = findViewById(R.id.imgDetallePromo);
        nombrePromo =  findViewById(R.id.title_promotxt);
        nombreLocal = findViewById(R.id.txtLocalNameDet);
        categoria = findViewById(R.id.txtCatDetallePromo);
        descripcion =  findViewById(R.id.txtDescripDetalle);
        fechaInicio = findViewById(R.id.txtStarPromoDe);
        fechaFin = findViewById(R.id.txtEndPromoDe);
        btnRHome= findViewById(R.id.btnRHome);

        btnRHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        String id = getIntent().getStringExtra("id");

        Obtenerpromo(id);

    }
    public void Regresar(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void Obtenerpromo(String id){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Promociones").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Promocion promos = dataSnapshot.getValue(Promocion.class);
                nombrePromo.setText(promos.getTitulo());
                categoria.setText(promos.getCategoria());
                descripcion.setText(promos.getDescripcion());
                fechaInicio.setText(getString(R.string.iniciaPromo)+promos.getFechaInicio());
                fechaFin.setText(getString(R.string.terminaPromo)+promos.getFechaFinal());

                // Aquí es donde cargamos la imagen
                StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+ promos.getIdUser() + "" + id);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Usuarios").orderByChild("idUser").equalTo(promos.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                            Negocio negocio = childSnapshot.getValue(Negocio.class);
                            nombreLocal.setText(negocio.getNegocio());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                try{
                    promoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl2 = uri.toString();
                        Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(imgDetallePromo);
                    });

                }catch (Exception e){
                    Picasso.get().load(R.drawable.fondo_pordefecto).into(imgDetallePromo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error
            }
        });
    }
}

