package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
    Promocion promo2=new Promocion();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_promo);

        imgDetallePromo = (ImageView) findViewById(R.id.imgDetallePromo);
        nombrePromo = (TextView) findViewById(R.id.title_promotxt);
        nombreLocal = (TextView) findViewById(R.id.txtLocalNameDet);
        categoria = (TextView) findViewById(R.id.txtCatDetallePromo);
        descripcion = (TextView) findViewById(R.id.txtDescripDetalle);
        fechaInicio = (TextView) findViewById(R.id.txtStarPromoDe);
        fechaFin = (TextView) findViewById(R.id.txtEndPromoDe);

        String id = getIntent().getStringExtra("id");

        Obtenerpromo(id);
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
                fechaFin.setText(getString(R.string.terminaPromo)+promos.getFechaFin());

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
                    promoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl2 = uri.toString();
                            Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(imgDetallePromo);
                        }
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

