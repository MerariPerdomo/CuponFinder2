package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private TextInputEditText txtEmail, txtPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private String email="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPssw);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnNoCount);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, registro.class));
            }
        });
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(login.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(login.this, "No se pudo iniciar session", Toast.LENGTH_SHORT).show();
                }
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