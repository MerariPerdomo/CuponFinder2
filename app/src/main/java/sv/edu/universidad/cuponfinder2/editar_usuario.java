package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import sv.edu.universidad.cuponfinder2.Model.Usuarios;

public class editar_usuario extends AppCompatActivity {
    private TextInputEditText nombre, email, negocio, psw, cpsw;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        nombre = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        negocio = findViewById(R.id.txtLocal);
        psw = findViewById(R.id.txtPs);
        cpsw = findViewById(R.id.txtPs2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            mDatabase.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("nombre").getValue(String.class);
                    String email1 = snapshot.child("email").getValue(String.class);
                    String local = snapshot.child("negocio").getValue(String.class);

                    nombre.setText(name);
                    email.setText(email1);
                    negocio.setText(local);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void UpdateData(View view) {
        String contra = psw.getText().toString();
        String id = mAuth.getCurrentUser().getUid();
        String correoActual = user.getEmail();
        String name = Objects.requireNonNull(nombre.getText()).toString();
        String correo = Objects.requireNonNull(email.getText()).toString();
        String local = Objects.requireNonNull(negocio.getText()).toString();


        if (!contra.isEmpty()) {
            if(psw.equals(cpsw)){
                user.updatePassword(contra)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String contra = psw.getText().toString();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("password",contra);
                                    mDatabase.child("Usuarios").child(id).updateChildren(map);
                                    Toast.makeText(editar_usuario.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        if (!correoActual.equals(correo)){
            Toast.makeText(this, "entro a cambio", Toast.LENGTH_SHORT).show();
            user.updateEmail(correo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(editar_usuario.this, "Completo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
/*
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", name);
        map.put("email", correo);
        map.put("negocio", local);
        mDatabase.child("Usuarios").child(id).updateChildren(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editar_usuario.this, "No se pueden actualizar", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(editar_usuario.this, "Successful update", Toast.LENGTH_SHORT).show();
            }
        });*/
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }
}