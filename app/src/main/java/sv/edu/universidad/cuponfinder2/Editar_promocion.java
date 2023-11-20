package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Editar_promocion extends AppCompatActivity {
    Button btnRegresarEditarPromocion;
    private TextView etEditarFechaFinal, etEditaFechaInicio, etTitulo, etDescripcion;
    private DatabaseReference mDatabase;
    private AutoCompleteTextView txtSpinner;
    private FirebaseAuth mAuth;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    private FirebaseFirestore mfirestore;

    private StorageReference storageReference;
    ImageView imgPromo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        btnRegresarEditarPromocion= findViewById(R.id.btnRegresarEditarPromocion);
        btnRegresarEditarPromocion.setOnClickListener(v -> onBackPressed());
        etTitulo = findViewById(R.id.txtTitulo);
        etDescripcion = findViewById(R.id.txtDescripcion);
        txtSpinner = findViewById(R.id.SpinCategories);
        etEditaFechaInicio = findViewById(R.id.etEditarFechaInicio);
        etEditarFechaFinal = findViewById(R.id.etEditarFechaFinal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgPromo=findViewById(R.id.imageView6);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtSpinner.setAdapter(adapter);

        /*DATE PICKER*/
        etEditarFechaFinal= findViewById(R.id.etEditarFechaFinal);
        etEditaFechaInicio= findViewById(R.id.etEditarFechaInicio);

       etEditaFechaInicio.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Seleccione la fecha de inicio de su promocion");
            final MaterialDatePicker<Long> materialDatePicker = builder.build();

            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> etEditaFechaInicio.setText(materialDatePicker.getHeaderText()));

        });
        etEditarFechaFinal.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Seleccione la fecha de finalizacion de su promocion");
            final MaterialDatePicker<Long> materialDatePicker = builder.build();

            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> etEditarFechaFinal.setText(materialDatePicker.getHeaderText()));
        });
        String idPromo = getIntent().getStringExtra("idPromo");
        MostrarPromo(idPromo);
    }

    public void MostrarPromo(String idPromo){

        mDatabase.child("Promociones").child(idPromo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String titulo = snapshot.child("titulo").getValue(String.class);
                String descripcion = snapshot.child("descripcion").getValue(String.class);
                String Inicio = snapshot.child("fechaInicio").getValue(String.class);
                String Final = snapshot.child("fechaFinal").getValue(String.class);
                String categoria = snapshot.child("categoria").getValue(String.class);
                String idUser = snapshot.child("idUser").getValue(String.class);
                String idPromo = snapshot.child("idPromo").getValue(String.class);
                etTitulo.setText(titulo);
                etDescripcion.setText(descripcion);
                etEditaFechaInicio.setText(Inicio);
                etEditarFechaFinal.setText(Final);
                txtSpinner.setText(categoria);

                StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+ idUser + "" + idPromo);

                try{
                    promoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl2 = uri.toString();
                            Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(imgPromo);
                        }
                    });

                }catch (Exception e){
                    Picasso.get().load(R.drawable.fondo_pordefecto).into(imgPromo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Actualizar(View view) {

        String titulo = etTitulo.getText().toString();
        String categoria = txtSpinner.getText().toString();
        String descripcion =etDescripcion.getText().toString();
        String start = etEditaFechaInicio.getText().toString();
        String end = etEditarFechaFinal.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("categoria", categoria);
        map.put("descripcion", descripcion);
        map.put("fechaInicio", start);
        map.put("fechaFinal", end);
        map.put("titulo", titulo);
        /*
        mDatabase.child("Promociones").child(idProm).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Editar_promocion.this, "Successful update", Toast.LENGTH_SHORT).show();
            }
        });*/
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }

    public void Regresar(View view) {
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }
}