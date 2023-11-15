package sv.edu.universidad.cuponfinder2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Negocio;

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
//        holder.imagen.setImageResource(negocio.getImagen());
//        holder.perfil_negocio.setImageResource(negocio.getPerfil_negocio());
        StorageReference perfilRef = FirebaseStorage.getInstance().getReference("perfil/*" + negocio.getIdUser());

        try{
            perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    Picasso.get().load(imageUrl).error(R.drawable.perfil_estatico).into(holder.perfil_negocio);
                }
            });

        }catch (Exception e){
            Picasso.get().load(R.drawable.perfil_estatico).into(holder.perfil_negocio);
        }

    }
    public void setNegocios(List<Negocio> nuevosNegocios) {
        this.negocios = nuevosNegocios;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return negocios.size();
    }
}