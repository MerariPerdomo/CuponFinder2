package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Editar_promocion extends AppCompatActivity {
    Button btnRegresarEditarPromocion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        btnRegresarEditarPromocion=(Button) findViewById(R.id.btnRegresarEditarPromocion);
        btnRegresarEditarPromocion.setOnClickListener(v -> onBackPressed());
    }
}