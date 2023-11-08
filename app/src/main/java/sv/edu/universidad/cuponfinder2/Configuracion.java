package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Configuracion extends AppCompatActivity {
    Button btnRegresarConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        btnRegresarConfig=(Button) findViewById(R.id.btnRegresarConfig);
        btnRegresarConfig.setOnClickListener(v -> onBackPressed());
    }
}