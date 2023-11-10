package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class registro extends AppCompatActivity {

    private TextInputEditText txtEmail, txtNombre, txtNegocio, txtContra, txtContra2;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
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
}