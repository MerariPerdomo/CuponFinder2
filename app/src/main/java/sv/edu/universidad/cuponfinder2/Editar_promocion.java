package sv.edu.universidad.cuponfinder2;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class Editar_promocion extends AppCompatActivity {
    Button btnRegresarEditarPromocion;
    private TextView etEditarFechaFinal, etEditaFechaInicio, etTitulo, etDescripcion;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    private FirebaseFirestore mfirestore;

    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private AutoCompleteTextView etCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        btnRegresarEditarPromocion= findViewById(R.id.btnRegresarEditarPromocion);
        btnRegresarEditarPromocion.setOnClickListener(v -> onBackPressed());
        etTitulo = findViewById(R.id.txtTitulo);
        etDescripcion = findViewById(R.id.txtDescripcion);
        etCategoria = findViewById(R.id.SpinCategories);

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
        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            mDatabase.child("Promocion").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Promocion promocion = snapshot.getValue(Promocion.class);
                    String titulo1 = promocion.getTitulo();
                    String descripcion1 = promocion.getDescripcion();
                    String categoria1 = promocion.getCategoria();
                    String inicio1 = promocion.getFechaInicio();
                    String fin1 = promocion.getFechaFin();

                    etTitulo.setText(titulo1);
                    etDescripcion.setText(descripcion1);
                   // etEditaFechaInicio.setText(local);
                    etEditaFechaInicio.setText(inicio1);
                    etEditarFechaFinal.setText(fin1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void Actualizar(View view) {

        String titulo = etTitulo.getText().toString();
       // String categoria =
        String descripcion =etDescripcion.getText().toString();
        String start = etEditaFechaInicio.getText().toString();
        String end = etEditarFechaFinal.getText().toString();

        Map<String, Object> map = new HashMap<>();
        //map.put("categoria", categoria);
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
        });
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }*/
    }
}