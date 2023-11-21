package sv.edu.universidad.cuponfinder2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Promocion;


public class promotions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PromocionesAdapter adapter;
    private List<Promocion> promocions = new ArrayList<>();
    private TextView txtCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        recyclerView = findViewById(R.id.rvPromotiones2);
        txtCategoria = findViewById(R.id.categoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String title = getIntent().getStringExtra("title");
        txtCategoria.setText(title);
        search(title);

    }
    public void search(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Promociones");
        Query query = ref.orderByChild("categoria").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Promocion> promocions = new ArrayList<>();
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
                String dateToday = sdf.format(currentDate);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Promocion promotion = dataSnapshot1.getValue(Promocion.class);
                    if (promotion.getFechaInicio().compareTo(dateToday) <= 0 && promotion.getFechaFinal().compareTo(dateToday) >= 0) {
                        promocions.add(promotion);
                    }
                    if (adapter == null) {
                        adapter = new PromocionesAdapter(promocions);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // manejar error
            }
        });
    }

    public void Regresar(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}