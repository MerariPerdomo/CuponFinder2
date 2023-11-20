package sv.edu.universidad.cuponfinder2;

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

import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class detalle_promo extends AppCompatActivity {
    TextView nombrePromo, nombreLocal, categoria, descripcion, fechaInicio, fechaFin;
    ImageView imgDetallePromo;
    public List<Promocion> promocions;
    Promocion promo2=new Promocion();
    private StorageReference storageReference;
    Negocio negocio=new Negocio();

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
                fechaInicio.setText("Inicia: "+promos.getFechaInicio());
                fechaFin.setText("Termina: "+promos.getFechaFin());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error
            }
        });

        StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+ negocio.getIdUser() + "" + id);

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
}
