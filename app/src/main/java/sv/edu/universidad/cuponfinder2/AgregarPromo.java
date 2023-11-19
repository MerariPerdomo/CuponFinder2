package sv.edu.universidad.cuponfinder2;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import sv.edu.universidad.cuponfinder2.Model.Negocio;

public class AgregarPromo extends AppCompatActivity {
    private TextInputEditText txtTitulo, txtDescripcion, txtInicio, txtFinal;
    private AutoCompleteTextView txtSpinner;
    private ImageView imgPromo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;
    private DatabaseReference mDatabase;
    Uri image_url;
    private static final int COD_SEL_IMAGE =300;
    ProgressDialog progressDialog;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_promo);

        txtTitulo = findViewById(R.id.txtTitle);
        txtDescripcion = findViewById(R.id.txtDescription);
        txtSpinner = findViewById(R.id.SpinCategories);
        txtInicio = findViewById(R.id.inicioF);
        txtFinal=findViewById(R.id.finalF);
        imgPromo=findViewById(R.id.imgPromo);
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtSpinner.setAdapter(adapter);
    }

    public void llamarFecha(View view) {
        if (view.getId()==R.id.inicioF){
            showDatePickerDialog(txtInicio);
        }else{
            showDatePickerDialog(txtFinal);
        }
    }

    private void showDatePickerDialog(final TextInputEditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    public void GuardarPromo(View view) {
        String categoria = txtSpinner.getText().toString();
        String titulo = Objects.requireNonNull(txtTitulo.getText()).toString();
        String descripcion = Objects.requireNonNull(txtDescripcion.getText()).toString();
        String fechaInicio = txtInicio.getText().toString();
        String fechaFinal = txtInicio.getText().toString();

        if (titulo.isEmpty() && descripcion.isEmpty() && categoria.isEmpty() &&
                fechaInicio.isEmpty() && fechaFinal.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            String id = mAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Negocio usuarios = dataSnapshot.getValue(Negocio.class);
                    String idPromo = mDatabase.child("Promociones").push().getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("idUser", id);
                    map.put("titulo", titulo);
                    map.put("descripcion", descripcion);
                    map.put("categoria", categoria);
                    map.put("fechaInicio", fechaInicio);
                    map.put("fechaFinal", fechaFinal);
                    map.put("idPromo", idPromo);
                    mDatabase.child("Promociones").child(idPromo).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
                            String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
                            if (imageBitmapString != null) {
                                Bitmap imageBitmap = stringToBitmap(imageBitmapString);
                                byte[] data = bitmapToByte(imageBitmap);
                                StorageReference reference = storageReference.child("promocion/*" + id + "" + idPromo);
                                reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                                    map.put("foto", download_uri);
                                                    mfirestore.collection("promocion").document(idPromo).update(map);
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            Toast.makeText(AgregarPromo.this, "Se creo correctamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), vistaUsurio.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AgregarPromo.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            if (resultCode == RESULT_OK) {
                if (requestCode == COD_SEL_IMAGE) {
                    image_url = data.getData();
                    subirFoto(image_url);
                    Uri selectedImage = data.getData();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void subirFoto(Uri image_url){
            try {
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

        public void AgregarFoto(View view) {
            Intent i =new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, COD_SEL_IMAGE);
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
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
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

        public void cargarImagen() {
            progressDialog.setMessage("Actualizando foto");
            progressDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("CuponFinder2", MODE_PRIVATE);
            String imageBitmapString = sharedPreferences.getString("tempImageBitmap", null);
            if (imageBitmapString != null) {
                Bitmap imageBitmap = stringToBitmap(imageBitmapString);
                File tempFile = createTempFile(imageBitmap);
                Picasso.get().load(tempFile).into(imgPromo);
                progressDialog.dismiss();
            }
        }

        public File createTempFile(Bitmap bitmap) {
            File tempFile = null;
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

    public void Cancelar(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}