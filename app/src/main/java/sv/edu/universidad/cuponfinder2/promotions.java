package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;


public class promotions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PromocionesAdapter adapter;
    private List<Promocion> promocions = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.rvPromotiones2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String title = getIntent().getStringExtra("title");
        if(title.isEmpty()) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Promociones");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    promocions.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Promocion promotion = dataSnapshot1.getValue(Promocion.class);
                        promocions.add(promotion);
                    }
                    if (adapter == null) {
                        adapter = new PromocionesAdapter(promocions);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ERROR", databaseError.getMessage());
                }
            });
        }else{
            search(title);
        }
    }
    public void search(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Promociones");
        Query query = ref.orderByChild("categoria").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Promocion> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Promocion promo = ds.getValue(Promocion.class);
                    list.add(promo);
                }
                PromocionesAdapter adapter = new PromocionesAdapter(list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // manejar error
            }
        });
    }
}