package sv.edu.universidad.cuponfinder2.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.universidad.cuponfinder2.Model.Promocion;
import sv.edu.universidad.cuponfinder2.R;

public class PromosAdaptor extends RecyclerView.Adapter<PromosAdaptor.PromoHolder>{

    Context context;
    ArrayList<Promocion> promocionArrayList;

    public PromosAdaptor(Context context, ArrayList<Promocion> promocionArrayList) {
        this.context = context;
        this.promocionArrayList = promocionArrayList;
    }

    @NonNull
    @Override
    public PromosAdaptor.PromoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_detalle_promo,parent,false);
        return new PromoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PromosAdaptor.PromoHolder holder, int position) {
        Promocion promocion =promocionArrayList.get(position);
        //holder.categoria.setText(promocion.);
        //holder.descripcion.setText(promocion.);
        //holder.fechaFinal.setText(promocion.); Aqui van los campos de las tablas del modelo
        //holder.fechaInicio.setText(promocion.);
        //holder.titulo.setText(promocion.);

    }

    @Override
    public int getItemCount() {
        return promocionArrayList.size();
    }

    public class PromoHolder extends RecyclerView.ViewHolder {

        TextView categoria, descripcion, fechaFinal, fechaInicio, titulo;
        public PromoHolder(@NonNull View itemView) {
            super(itemView);
            categoria = itemView.findViewById(R.id.txtCatDetallePromo);
            descripcion=itemView.findViewById(R.id.txtDescripDetalle);
            fechaFinal= itemView.findViewById(R.id.txtEndPromoDe);
            fechaInicio = itemView.findViewById(R.id.txtStarPromoDe);
            titulo = itemView.findViewById(R.id.title_promotxt);
        }
    }
}
