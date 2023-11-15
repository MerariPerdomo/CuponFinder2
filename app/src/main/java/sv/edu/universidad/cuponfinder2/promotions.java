package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Promocion;


public class promotions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PromocionesAdapter adapter;
    private List<Promocion> promocions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        recyclerView = findViewById(R.id.rvPromotiones2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    }


    public void AgregarPromo(View view) {
        Intent intent = new Intent(getApplicationContext(), AgregarPromo.class);
        startActivity(intent);
    }
}