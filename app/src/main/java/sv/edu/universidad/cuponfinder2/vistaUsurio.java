package sv.edu.universidad.cuponfinder2;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import sv.edu.universidad.cuponfinder2.Model.Usuarios;

public class vistaUsurio extends AppCompatActivity {
    private TextView nombreUsuario, email;
    private Button btnEditar;
    private ImageView perfilFoto;
    private FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_usurio);
        nombreUsuario = findViewById(R.id.Username);
        email=findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        btnEditar = (Button) findViewById(R.id.btnEditarPerfil);
        perfilFoto = findViewById(R.id.img_perfil_negocio);

        storageReference= FirebaseStorage.getInstance().getReference();

//        btnCerrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                startActivity(new Intent(getApplicationContext(), login.class));
//                finish();
//            }
//        });
        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                    nombreUsuario.setText(usuarios.getNombre());
                    email.setText(usuarios.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Manejar error
                }
            });
            StorageReference reference = storageReference.child("perfil/*" + id);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Foto de perfil
                    String imageUrl = uri.toString();
                    Picasso.get().load(imageUrl).into(perfilFoto);
                }
            });
        }else{
            nombreUsuario.setText("Sin usuario");
            email.setText("sin email");
        }

    }

    public void Editar(View view) {
        Intent i = new Intent(getApplicationContext(),editar_usuario.class);
        startActivity(i);
    }



//    public void ViewSettings(View view) {
//        Intent ir = new Intent(vistaUsurio.this,Configuracion.class);
//        startActivity(ir);
//    }
//
//    public void ViewAboutUs(View view) {
//        Intent ir = new Intent(vistaUsurio.this, AcercaDe.class);
//        startActivity(ir);
//    }
}