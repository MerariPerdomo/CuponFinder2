package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import sv.edu.universidad.cuponfinder2.Model.Promocion;
import sv.edu.universidad.cuponfinder2.Model.Usuarios;

public class Editar_promocion extends AppCompatActivity {
    Button btnRegresarEditarPromocion;
    private TextView etEditarFechaFinal, etEditaFechaInicio, etTitulo, etDescripcion;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        btnRegresarEditarPromocion= findViewById(R.id.btnRegresarEditarPromocion);
        btnRegresarEditarPromocion.setOnClickListener(v -> onBackPressed());
        etTitulo = findViewById(R.id.txtTitulo);
        etDescripcion = findViewById(R.id.txtDescripcion);

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
                    String titulo1 = snapshot.child("nombre").getValue(String.class);
                    String descripcion1 = snapshot.child("email").getValue(String.class);
               //     String categoria1 = snapshot.child("negocio").getValue(String.class);
                    Promocion promocion = snapshot.getValue(Promocion.class);
                    String inicio = promocion.getFechaInicio();
                    String fin = promocion.getFechaFin();

                    etTitulo.setText(titulo1);
                    etDescripcion.setText(descripcion1);
                   // etEditaFechaInicio.setText(local);
                    etEditaFechaInicio.setText(inicio);
                    etEditarFechaFinal.setText(fin);
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