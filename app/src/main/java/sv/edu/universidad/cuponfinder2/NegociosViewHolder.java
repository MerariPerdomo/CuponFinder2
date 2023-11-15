package sv.edu.universidad.cuponfinder2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class NegociosViewHolder extends RecyclerView.ViewHolder {

    TextView nombre, categoria;
    ImageView imagen;
    CircleImageView perfil_negocio;

    public NegociosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.etNombre);
        imagen = itemView.findViewById(R.id.imgEjemplo);
        perfil_negocio=itemView.findViewById(R.id.img_perfil_negocio);

    }
}