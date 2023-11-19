package sv.edu.universidad.cuponfinder2.Adaptor;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import sv.edu.universidad.cuponfinder2.Editar_promocion;
import sv.edu.universidad.cuponfinder2.Model.Negocio;
import sv.edu.universidad.cuponfinder2.Model.Promocion;
import sv.edu.universidad.cuponfinder2.PromocionesHolder;
import sv.edu.universidad.cuponfinder2.R;
import sv.edu.universidad.cuponfinder2.editar_usuario;
import sv.edu.universidad.cuponfinder2.vistaNegocio;

public class MypromoAdaptor extends RecyclerView.Adapter<MypromoAdaptor.MypromoHolder> {
    public List<Promocion> promocions;
    Promocion promo =new Promocion();
    public MypromoAdaptor(List<Promocion> promocions) {
        this.promocions = promocions;
    }

    @NonNull
    @Override
    public MypromoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotionuser_recycle, parent, false);
        return new MypromoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MypromoHolder holder, int position) {
        Promocion promocion = promocions.get(position);
        holder.txtTituloPromo.setText(promocion.getTitulo());
        String idPromo = promocion.getIdPromo();
        String idUser = promocion.getIdUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Usuarios").orderByChild("idUser").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    Negocio negocio = childSnapshot.getValue(Negocio.class);
                    holder.txtTitleLocalName.setText(negocio.getNegocio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*Foto de la promo*/
        StorageReference promoRef = FirebaseStorage.getInstance().getReference("promocion/*"+ idUser + "" + idPromo);

        try{
            promoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl2 = uri.toString();
                    Picasso.get().load(imageUrl2).error(R.drawable.fondo_pordefecto).into(holder.img_fotopromo);
                }
            });

        }catch (Exception e){
            Picasso.get().load(R.drawable.fondo_pordefecto).into(holder.img_fotopromo);
        }
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Editar_promocion.class);
                i.putExtra("idPromo", idPromo);
                v.getContext().startActivity(i);
            }
        });

    }
    public void setPromocions(List<Promocion> nuevasPromociones) {
        this.promocions = nuevasPromociones;
        notifyDataSetChanged();}

    @Override
    public int getItemCount() {
        return promocions.size();
    }

    public class MypromoHolder extends RecyclerView.ViewHolder {

        Button btnEditar, btnBorrar;
        TextView txtTituloPromo, txtTitleLocalName;
        ImageView img_fotopromo;

        public MypromoHolder(@NonNull View itemView) {
            super(itemView);
            txtTituloPromo = itemView.findViewById(R.id.txtTituloPromo);
            txtTitleLocalName = itemView.findViewById(R.id.txtTitleLocalName);
            img_fotopromo = itemView.findViewById(R.id.img_fotopromo);
            btnEditar = itemView.findViewById(R.id.btnEliminarPromo);
            btnBorrar = itemView.findViewById(R.id.btnEliminarPromo);
        }
    }
}
