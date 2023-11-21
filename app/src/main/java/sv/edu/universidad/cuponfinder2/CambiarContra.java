package sv.edu.universidad.cuponfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class CambiarContra extends AppCompatActivity {
    private TextInputEditText txtemail;
    private Button btnRegresar;
    private FirebaseAuth mAuth;
    private String email ="";
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);
        txtemail = findViewById(R.id.txtEmail);
        btnRegresar = findViewById(R.id.Regresar);
        btnRegresar.setOnClickListener(v -> onBackPressed());
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
    }

    public void Cambiar(View view) {
        email = txtemail.getText().toString();
        if (!email.isEmpty()){
            mDialog.setMessage(getString(R.string.please_wait_a_moment));
            mDialog.setCancelable(false);
            mDialog.show();;
            mAuth.setLanguageCode("en");
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), R.string.check_your_email, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.could_not_send_password_reset_email, Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), R.string.enter_your_email, Toast.LENGTH_SHORT).show();
        }
    }

}