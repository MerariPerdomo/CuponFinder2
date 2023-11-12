package sv.edu.universidad.cuponfinder2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NegociosAdapter extends RecyclerView.Adapter<NegociosViewHolder> {

    public List<Negocio> negocios;

    public NegociosAdapter(List<Negocio> negocios) {
        this.negocios = negocios;
    }

    @NonNull
    @Override
    public NegociosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.negocios_cardview, parent, false);
        return new NegociosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NegociosViewHolder holder, int position) {
        Negocio negocio = negocios.get(position);
        holder.nombre.setText(negocio.getNombre());
        holder.categoria.setText(negocio.getCategoria());
        holder.imagen.setImageResource(negocio.getImagen());
    }

    @Override
    public int getItemCount() {
        return negocios.size();
    }
}