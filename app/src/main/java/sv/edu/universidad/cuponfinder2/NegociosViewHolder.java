package sv.edu.universidad.cuponfinder2;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class NegociosViewHolder extends RecyclerView.ViewHolder {

    TextView nombre;
    CircleImageView perfil_negocio;
    Button verNegocio;
    ImageView fondo_negocio;

    public NegociosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.etNombre);
        perfil_negocio=itemView.findViewById(R.id.img_perfil_negocio);
        fondo_negocio=itemView.findViewById(R.id.imgEjemplo);
        verNegocio=itemView.findViewById(R.id.verNegocio);
    }
}