package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class vistaUsurio extends AppCompatActivity {
    private Button btnCerrar;
    private ImageView perfilFoto;
    private FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_usurio);

        mAuth = FirebaseAuth.getInstance();
        btnCerrar = (Button) findViewById(R.id.btnSession);
        perfilFoto = findViewById(R.id.profile_image);
        storageReference= FirebaseStorage.getInstance().getReference();

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            StorageReference reference = storageReference.child("perfil/*" + id);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    Picasso.with(getApplicationContext()).load(imageUrl).into(perfilFoto);
                }
            });
        }
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