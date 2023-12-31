package sv.edu.universidad.cuponfinder2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {

    private TextInputEditText txtEmail, txtNombre, txtNegocio, txtContra, txtContra2;
    private ImageView perfil;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    private static final int COD_SEL_IMAGE =300;
    private Uri image_url;
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
        txtEmail = findViewById(R.id.txtEmail);
        txtNombre = findViewById(R.id.txtName);
        txtNegocio = findViewById(R.id.txtLocal);
        txtContra = findViewById(R.id.txtPs);
        txtContra2 = findViewById(R.id.txtPs2);
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        perfil=findViewById(R.id.imgUsuario);

    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id =mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("idUser", id);
                map.put("email",email);
                map.put("nombre", nombre);
                map.put("negocio", negocio);
                mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        startActivity(new Intent(registro.this, vistaUsurio.class));
                        finish();
                    }else{
                        Toast.makeText(registro.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                    }
                });
                SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
                String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
                if (imageBitmapString != null) {
                    Bitmap imageBitmap = stringToBitmap(imageBitmapString);
                    byte[] data = bitmapToByte(imageBitmap);
                    StorageReference reference = storageReference.child("perfil/*" + id);
                    reference.putBytes(data).addOnSuccessListener(taskSnapshot -> {

                    }).addOnFailureListener(e -> Toast.makeText(registro.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
                }

            } else {
                Toast.makeText(registro.this, "Authentication failed", Toast.LENGTH_SHORT).show();
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
                data.getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);
    }
    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
    private void subirFoto(Uri image_url) {
        try{
            InputStream imageStream = getContentResolver().openInputStream(image_url);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("tempImageBitmap", bitmapToString(selectedImage));
            editor.apply();
            cargarImagen();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(registro.this, "Error al subir la foto", Toast.LENGTH_SHORT).show();
        }
    }
    public void cargarImagen() {
        SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
        String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
        if (imageBitmapString != null) {
            Bitmap imageBitmap = stringToBitmap(imageBitmapString);
            File tempFile = createTempFile(imageBitmap);
            Picasso.get().load(tempFile).into(perfil);
        }
    }

    public File createTempFile(Bitmap bitmap) {
        File tempFile;
        try {
            tempFile = File.createTempFile("tempImage", ".png", getCacheDir());
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}