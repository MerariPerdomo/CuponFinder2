package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class promotions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
    }

    public void AgregarPromo(View view) {
        Intent intent = new Intent(getApplicationContext(), AgregarPromo.class);
        startActivity(intent);
    }
}