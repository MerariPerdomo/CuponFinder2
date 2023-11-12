package sv.edu.universidad.cuponfinder2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;

public class Editar_promocion extends AppCompatActivity {
    Button btnRegresarEditarPromocion;
    TextView etEditarFechaFinal;
    TextView etEditaFechaInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        btnRegresarEditarPromocion= findViewById(R.id.btnRegresarEditarPromocion);
        btnRegresarEditarPromocion.setOnClickListener(v -> onBackPressed());

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

    }
}