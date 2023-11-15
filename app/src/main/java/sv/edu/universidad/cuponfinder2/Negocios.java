package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Negocio;

public class Negocios extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NegociosAdapter adapter;
    private List<Negocio> negocios = new ArrayList<>();

    Button btnRegresarNegocios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);

        btnRegresarNegocios = findViewById(R.id.btnRegresarNegocios);

        btnRegresarNegocios.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.rvNegocios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Negocio> nuevosNegocios = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Negocio negocio = dataSnapshot1.getValue(Negocio.class);
                    nuevosNegocios.add(negocio);
                }
                if (adapter == null) {
                    adapter = new NegociosAdapter(nuevosNegocios);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setNegocios(nuevosNegocios);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", databaseError.getMessage());
            }
        });

    }
}
