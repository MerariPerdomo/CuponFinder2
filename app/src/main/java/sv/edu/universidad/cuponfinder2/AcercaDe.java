package sv.edu.universidad.cuponfinder2;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaDe extends AppCompatActivity {

    Button btnRegresarAcercaDe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        btnRegresarAcercaDe= findViewById(R.id.btnRegresarAcercaDe);
        btnRegresarAcercaDe.setOnClickListener(v -> onBackPressed());
    }
}