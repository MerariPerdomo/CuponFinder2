package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Negocios extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NegociosAdapter adapter;
    private List<Negocio> negocios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);

        negocios = obtenerNegocios();

        recyclerView = findViewById(R.id.rvNegocios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NegociosAdapter(negocios);
        recyclerView.setAdapter(adapter);
    }
    public List<Negocio> obtenerNegocios() {
        negocios = new ArrayList<>();
        // Agregar algunos productos a la lista
        negocios.add(new Negocio("Nombre del producto 1", "Categoría del producto 1", R.drawable.publicidadejemplo));
        negocios.add(new Negocio("Nombre del producto 2", "Categoría del producto 2", R.drawable.publicidadejemplo));
        // Puedes agregar más productos aquí

        // Devolver la lista de productos
        return negocios;
    }
}