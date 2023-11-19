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

import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class PromocionesAdapter extends RecyclerView.Adapter<PromocionesHolder> {

    public List<Promocion> promocions;
    Promocion promo =new Promocion();
    public PromocionesAdapter(List<Promocion> promocions) {
        this.promocions = promocions;
    }
    @NonNull
    @Override
    public PromocionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_recycle, parent, false);
        return new PromocionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromocionesHolder holder, int position) {
        Promocion promocion = promocions.get(position);

        holder.txtTituloPromo.setText(promocion.getTitulo());
        holder.txtTitleLocalName.setText(promocion.getNegocio());
//        holder.txtTitleLocalName.setText(negocio.ge);
//        holder.imagen.setImageResource(negocio.getImagen());
//        holder.perfil_negocio.setImageResource(negocio.getPerfil_negocio());


        /*Foto de la promo*/
//        StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+promocion.getIdUser());
//
//        try{
//            promoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    String imageUrl2 = uri.toString();
//                    Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(holder.img_fotopromo);
//                }
//            });
//
//        }catch (Exception e){
//            Picasso.get().load(R.drawable.fondo_pordefecto).into(holder.img_fotopromo);
//        }
        /*Foto del negocio*/
//        StorageReference tiendaRef = FirebaseStorage.getInstance().getReference("perfil/*"+promocion.getIdUser());
//
//        try{
//            tiendaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    String imageUrl = uri.toString();
//                    Picasso.get().load(imageUrl).error(R.drawable.perfil_estatico).into(holder.img_perfilnegocio);
//                }
//            });
//
//        }catch (Exception e){
//            Picasso.get().load(R.drawable.perfil_estatico).into(holder.img_perfilnegocio);
//        }

    }
    public void setPromocions(List<Promocion> nuevasPromociones) {
        this.promocions = nuevasPromociones;
        notifyDataSetChanged();}

    @Override
    public int getItemCount() {
        return promocions.size();
    }
}
