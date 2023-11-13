package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class promotions extends AppCompatActivity {

    Button btnRHome,btnAgregarPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        btnRHome=(Button) findViewById(R.id.btnRHome);
        btnRHome.setOnClickListener(v -> onBackPressed());
    }
}