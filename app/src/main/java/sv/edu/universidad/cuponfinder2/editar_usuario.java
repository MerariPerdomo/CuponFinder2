package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editar_usuario extends AppCompatActivity {
    private TextInputEditText nombre, email, negocio, psw, cpsw;
    private ImageView fotoPerfil, fotoFondo;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    private FirebaseFirestore mfirestore;

    ProgressDialog progressDialog;
    StorageReference storageReference;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        fotoPerfil= findViewById(R.id.imgUsuario);
        fotoFondo = findViewById(R.id.imgFondo);
        nombre = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        negocio = findViewById(R.id.txtLocal);
        psw = findViewById(R.id.txtPs);
        cpsw = findViewById(R.id.txtPs2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            String id = mAuth.getCurrentUser().getUid();
            Picasso.get().load("perfil/*"+id).into(fotoPerfil);
            Picasso.get().load("fondo/*"+id).into(fotoFondo);
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
        });
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }

    public void ActualizarFoto(View view) {
        url = "perfil/*";
        cargarFoto();
    }

    public void ActualizarFondo(View view) {
        url = "fondo/*";
        cargarFoto();
    }
    private void cargarFoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }
    public void cargarImagen(Uri imageUrl) {
        if(url.equals("perfil/*")){
            Picasso.get().load(imageUrl).into(fotoPerfil);
        } else if (url.equals("fondo/*")) {
            Picasso.get().load(imageUrl).into(fotoFondo);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirFoto(image_url, url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirFoto(Uri image_url, String url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = url + mAuth.getUid();
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            cargarImagen(image_url);
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Cancelar(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}