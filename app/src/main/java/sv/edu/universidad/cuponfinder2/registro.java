package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class registro extends AppCompatActivity {

    private TextInputEditText txtEmail, txtNombre, txtNegocio, txtContra, txtContra2;
    private ImageView perfil;
    private Button agregar;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseFirestore mfirestore;
    String storage_path = "perfil/*";
    private static final int COD_SEL_STORAGE =200;
    private static final int COD_SEL_IMAGE =300;
    private Uri image_url;
    String photo = "photo";
    String idd;
    ProgressDialog progressDialog;
    //Valores a registrar
    private String nombre ="";
    private String email ="";
    private String password ="";
    private String negocio ="";
    private String password2 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        mfirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        agregar = findViewById(R.id.agregarFoto);
        perfil=findViewById(R.id.imgUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtNombre = findViewById(R.id.txtName);
        txtNegocio = findViewById(R.id.txtLocal);
        txtContra = findViewById(R.id.txtPs);
        txtContra2 = findViewById(R.id.txtPs2);

    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("password", password);
                    map.put("nombre", nombre);
                    map.put("negocio", negocio);

                    String id =mAuth.getCurrentUser().getUid();
                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(registro.this, vistaUsurio.class));
                                finish();
                             /*   SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
                                String imageUriString = sharedPreferences.getString("imageUri", null);
                                if (imageUriString != null) {
                                    Uri imageUri = Uri.parse(imageUriString);
                                    StorageReference reference = storageReference.child("perfil/*" + id);
                                    reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isSuccessful());
                                            if (uriTask.isSuccessful()){
                                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String download_uri = uri.toString();
                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("photo", download_uri);
                                                        mfirestore.collection("perfil").document(mAuth.getUid()).update(map);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }*/
                            }else{
                                Toast.makeText(registro.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(registro.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void accountExist(View view) {
        Intent Login = new Intent(registro.this, login.class);
        startActivity(Login);
    }

    public void CreateCount(View view) {

        email = txtEmail.getText().toString();
        nombre = txtNombre.getText().toString();
        negocio = txtNegocio.getText().toString();
        password = txtContra.getText().toString();
        password2 = txtContra2.getText().toString();

        if (txtEmail.getText().toString().isEmpty()|| txtContra.getText().toString().isEmpty()
                ||txtContra2.getText().toString().isEmpty()||txtNegocio.getText().toString().isEmpty()
                ||txtNombre.getText().toString().isEmpty()){
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            if (password2.equals(password)) {
                if (password.length() >= 6) {
                    registerUser();
                } else {
                    Toast.makeText(registro.this, "La contraseña debe tener almenos 6 caracteres ", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(registro.this, "La confirmacion debe ser igual a la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    public void AgregarFoto(View view) {
        Intent i =new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirFoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirFoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + "photo" + "" + mAuth.getUid();
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
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("perfil").document(mAuth.getUid()).update(map);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registro.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();

            }
        });
        String tempImageName = "tempImage";
        StorageReference reference = storageReference.child(tempImageName);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            // Guarda la URL de la imagen en SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                            sharedPreferences.edit().putString("tempImageUrl", download_uri).apply();
                            Toast.makeText(registro.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            cargarImagen();
                        }
                    });
                }
            }
        });
    }
    public void cargarImagen() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("tempImageUrl", null);
        if (imageUrl != null) {
            Picasso.with(getApplicationContext()).load(imageUrl).into(perfil);
        }

    }*/
}