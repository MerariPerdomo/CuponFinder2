package sv.edu.universidad.cuponfinder2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NegociosViewHolder extends RecyclerView.ViewHolder {

    TextView nombre, categoria;
    ImageView imagen;

    public NegociosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.etNombre);
        categoria = itemView.findViewById(R.id.tvCategory);
        imagen = itemView.findViewById(R.id.imgEjemplo);
    }
}