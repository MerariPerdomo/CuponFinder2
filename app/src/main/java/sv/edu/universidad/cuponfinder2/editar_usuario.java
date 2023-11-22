package sv.edu.universidad.cuponfinder2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
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
    private TextInputEditText nombre, negocio;
    private ImageView fotoPerfil, fotoFondo;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;

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
        negocio = findViewById(R.id.txtLocal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference();
        String id = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser() != null) {
            MostrarDatos(id);
        }
    }

    private void MostrarDatos(String id) {
        mDatabase.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("nombre").getValue(String.class);
                String local = snapshot.child("negocio").getValue(String.class);

                nombre.setText(name);
                negocio.setText(local);
                StorageReference perfil = FirebaseStorage.getInstance().getReference("perfil/*"+ id);

                try{
                    perfil.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl2 = uri.toString();
                            Picasso.get().load(imageUrl2).error(R.drawable.perfil_estatico).into(fotoPerfil);
                        }
                    });

                }catch (Exception e){
                    Picasso.get().load(R.drawable.perfil_estatico).into(fotoPerfil);
                }
                StorageReference fondo = FirebaseStorage.getInstance().getReference("fondo/*"+ id);
                try{
                    fondo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl2 = uri.toString();
                            Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(fotoFondo);
                        }
                    });

                }catch (Exception e){
                    Picasso.get().load(R.drawable.fondo_pordefecto).into(fotoFondo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void UpdateData(View view) {
        String id = mAuth.getCurrentUser().getUid();
        String name = Objects.requireNonNull(nombre.getText()).toString();
        String local = Objects.requireNonNull(negocio.getText()).toString();

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", name);
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
            subirFoto(image_url, url);
        } else if (url.equals("fondo/*")) {
            Picasso.get().load(imageUrl).into(fotoFondo);
            subirFoto(image_url, url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                cargarImagen(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void guardarImagenEnSharedPreferences(Uri image_url, String url) {
//        String imageUriString = image_url.toString();
//        SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(url, imageUriString);
//        editor.apply();
//        cargarImagen(image_url);
//    }

    private void subirFoto(Uri image_url, String url) {
        String id = mAuth.getCurrentUser().getUid();
        StorageReference reference = storageReference.child(url + id);
        UploadTask uploadTask = reference.putFile(image_url);
    }


    public void Cancelar(View view) {
        Intent intent = new Intent(getApplicationContext(), vistaUsurio.class);
        startActivity(intent);
    }

    public void CambiarContra(View view) {
        Intent intent = new Intent(getApplicationContext(), CambiarContra.class);
        startActivity(intent);
    }
}