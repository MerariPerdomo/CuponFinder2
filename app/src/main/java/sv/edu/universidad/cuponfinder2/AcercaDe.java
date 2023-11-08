package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AcercaDe extends AppCompatActivity {

    Button btnRegresarAcercaDe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        btnRegresarAcercaDe=(Button) findViewById(R.id.btnRegresarAcercaDe);
        btnRegresarAcercaDe.setOnClickListener(v -> onBackPressed());
    }
}