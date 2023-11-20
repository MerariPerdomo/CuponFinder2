package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Negocio;

public class Negocios extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerView;
    private NegociosAdapter adapter;
    private SearchView searchView;
    private Button btnRegresarNegocios;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);

        btnRegresarNegocios = findViewById(R.id.btnRegresarNegocios);
        btnRegresarNegocios.setOnClickListener(v -> onBackPressed());
        searchView = findViewById(R.id.searchView);

        recyclerView = findViewById(R.id.rvNegocios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");

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

        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    public void search(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        Query query = ref.orderByChild("negocio").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Negocio> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Negocio negocio = ds.getValue(Negocio.class);
                    list.add(negocio);
                }
                NegociosAdapter adapter = new NegociosAdapter(list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // manejar error
            }
        });
    }

}
