package sv.edu.universidad.cuponfinder2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


public class Editar_promocion extends AppCompatActivity {
    private TextView  etTitulo, etDescripcion;
    private TextInputEditText etEditarFechaFinal, etEditaFechaInicio;
    private DatabaseReference mDatabase;
    private AutoCompleteTextView txtSpinner;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;

    private StorageReference storageReference;
    String idPromo, idUser;
    ImageView imgPromo;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_promocion);
        etTitulo = findViewById(R.id.txtTitulo);
        etDescripcion = findViewById(R.id.txtDescripcion);
        txtSpinner = findViewById(R.id.SpinCategories);
        etEditaFechaInicio = findViewById(R.id.etEditarFechaInicio);
        etEditarFechaFinal = findViewById(R.id.etEditarFechaFinal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgPromo=findViewById(R.id.imageView6);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtSpinner.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
        preferences.edit().clear().commit();


        idPromo = getIntent().getStringExtra("idPromo");
        idUser = getIntent().getStringExtra("idUser");
        MostrarPromo(idPromo);
    }

    public void MostrarPromo(String idPromo){

        mDatabase.child("Promociones").child(idPromo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String titulo = snapshot.child("titulo").getValue(String.class);
                String descripcion = snapshot.child("descripcion").getValue(String.class);
                String Inicio = snapshot.child("fechaInicio").getValue(String.class);
                String Final = snapshot.child("fechaFinal").getValue(String.class);
                String idUser = snapshot.child("idUser").getValue(String.class);
                etTitulo.setText(titulo);
                etDescripcion.setText(descripcion);
                etEditaFechaInicio.setText(Inicio);
                etEditarFechaFinal.setText(Final);

                StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+ idUser + "" + idPromo);

                try{
                    promoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl2 = uri.toString();
                        Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(imgPromo);
                    });

                }catch (Exception e){
                    Picasso.get().load(R.drawable.fondo_pordefecto).into(imgPromo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Actualizar(View view) {
        String titulo = etTitulo.getText().toString();
        String categoria = txtSpinner.getText().toString();
        String descripcion =etDescripcion.getText().toString();
        String start = etEditaFechaInicio.getText().toString();
        String end = etEditarFechaFinal.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("descripcion", descripcion);
        map.put("fechaInicio", start);
        map.put("fechaFinal", end);
        map.put("titulo", titulo);
        if(!categoria.equals("")){map.put("categoria", categoria);}

        mDatabase.child("Promociones").child(idPromo).updateChildren(map).addOnSuccessListener(unused -> {
                SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
                String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
                if (imageBitmapString != null) {
                    Bitmap imageBitmap = stringToBitmap(imageBitmapString);
                    byte[] data = bitmapToByte(imageBitmap);
                    StorageReference reference = storageReference.child("promocion/*" + idUser + "" + idPromo);
                    reference.putBytes(data);
                }
            Toast.makeText(Editar_promocion.this, R.string.successful_update, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), vistaUsurio.class);
            startActivity(i);
        }).addOnFailureListener(e -> {
            Toast.makeText(Editar_promocion.this, "Error", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), vistaUsurio.class);
            startActivity(i);
        });
    }
    public void llamarFecha(View view) {
        if (view.getId()==R.id.etEditarFechaInicio){
            showDatePickerDialog(etEditaFechaInicio);
        }else{
            showDatePickerDialog(etEditarFechaFinal);
        }
    }

    private void showDatePickerDialog(final TextInputEditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((view, year, month, day) -> {
            final String selectedDate = day + " / " + (month+1) + " / " + year;
            editText.setText(selectedDate);
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    public void Regresar(View view) {
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
    }

    public void Cancelar(View view) {
        Intent i = new Intent(getApplicationContext(),vistaUsurio.class);
        startActivity(i);
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
                assert data != null;
                image_url = data.getData();
                subirFoto(image_url);
                Uri selectedImage = data.getData();
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
            Toast.makeText(getApplicationContext(), "Error al subir la foto", Toast.LENGTH_SHORT).show();
        }
    }
    public void cargarImagen() {
        SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
        String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
        if (imageBitmapString != null) {
            Bitmap imageBitmap = stringToBitmap(imageBitmapString);
            File tempFile = createTempFile(imageBitmap);
            Picasso.get().load(tempFile).into(imgPromo);
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