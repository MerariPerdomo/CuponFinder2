package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private TextInputEditText txtEmail, txtPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private String email="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPssw);
        btnRegister=findViewById(R.id.btnNoCount);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v -> startActivity(new Intent(login.this, registro.class)));
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(login.this, MainActivity.class));
                finish();
            }else{
                Toast.makeText(login.this, "No se pudo iniciar session", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ViewSignUp(View view) {
        Intent signUp = new Intent(getApplicationContext(), registro.class);
        startActivity(signUp);
    }

    public void OlvidoContra(View view) {
        Intent intent = new Intent(getApplicationContext(), CambiarContra.class);
        startActivity(intent);
    }

    public void Login(View view) {
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            loginUser();
        }
    }
}