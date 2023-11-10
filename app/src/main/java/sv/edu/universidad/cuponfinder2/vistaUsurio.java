package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class vistaUsurio extends AppCompatActivity {
    private Button btnCerrar;
    private FirebaseAuth mAunth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_usurio);

        mAunth = FirebaseAuth.getInstance();
        btnCerrar = (Button) findViewById(R.id.btnSession);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAunth.signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
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