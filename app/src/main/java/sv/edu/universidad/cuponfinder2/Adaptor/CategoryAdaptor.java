package sv.edu.universidad.cuponfinder2.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sv.edu.universidad.cuponfinder2.R;
import sv.edu.universidad.cuponfinder2.domain.CategoryDomain;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.ViewHolder> {
    ArrayList<CategoryDomain>categoryDomains;
    private final OnItemClickListener listener;

    public CategoryAdaptor(ArrayList<CategoryDomain> categoryDomains, OnItemClickListener listener) {
        this.categoryDomains = categoryDomains;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(String title);
    }
    @NonNull
    @Override
    public CategoryAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_recycle,parent,false);
        return new ViewHolder(inflate, listener);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryAdaptor.ViewHolder holder, int position) {
        holder.categoriaNombre.setText(categoryDomains.get(position).getTitulo());


        int[] drawables = new int[]{
                R.drawable.coffe_c6,
                R.drawable.marketplace_c2,
                R.drawable.healt_c3,
                R.drawable.pets_c4,
                R.drawable.tech_c,
                R.drawable.ropa_c6
        };


        int[] fondos = new int[]{
                R.drawable.fondo_categoria1,
                R.drawable.fondo_categoria2,
                R.drawable.fondo_categoria3,
                R.drawable.fondo_categoria4,
                R.drawable.fondo_categoria5,
                R.drawable.fondo_categoria6
        };


        if (position < drawables.length) {
            holder.principal.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), fondos[position]));

            Glide.with(holder.itemView.getContext())
                    .load(drawables[position])
                    .into(holder.categoriaPhoto);
        }
    }


    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoriaNombre;
        ImageView categoriaPhoto;
        ConstraintLayout principal;
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            categoriaNombre=itemView.findViewById(R.id.NombreCategoria);
            categoriaPhoto=itemView.findViewById(R.id.PhotoCat);
            principal=itemView.findViewById(R.id.layoutCat1);
            principal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        String title = categoriaNombre.getText().toString();
                        listener.onItemClick(title);
                    }
                }
            });
        }
    }
}
