package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class vistaUsurio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_usurio);
    }

    public void ViewSettings(View view) {
        Intent ir = new Intent(vistaUsurio.this,Configuracion.class);
        startActivity(ir);
    }

    public void ViewAboutUs(View view) {
        Intent ir = new Intent(vistaUsurio.this, AcercaDe.class);
        startActivity(ir);
    }
}