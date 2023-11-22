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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
            mDialog.show();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");
            Query query = mDatabase.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        mAuth.setLanguageCode("en");
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), R.string.check_your_email, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.could_not_send_password_reset_email, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "No count exist with this email", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), R.string.enter_your_email, Toast.LENGTH_SHORT).show();
        }
        mDialog.dismiss();
    }

}