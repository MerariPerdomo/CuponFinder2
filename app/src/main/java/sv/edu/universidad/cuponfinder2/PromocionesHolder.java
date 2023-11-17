package sv.edu.universidad.cuponfinder2;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PromocionesHolder extends RecyclerView.ViewHolder {

    /*Definiciones*/
    TextView txtTituloPromo, txtTitleLocalName;
    CircleImageView img_perfilnegocio;
    ImageView img_fotopromo;

    public PromocionesHolder(@NonNull View itemView) {
        super(itemView);
        txtTituloPromo = itemView.findViewById(R.id.txtTituloPromo);
        txtTitleLocalName = itemView.findViewById(R.id.txtTitleLocalName);
        img_perfilnegocio=itemView.findViewById(R.id.img_perfil_negocio);
        img_fotopromo=itemView.findViewById(R.id.img_fotopromo);
    }
}
