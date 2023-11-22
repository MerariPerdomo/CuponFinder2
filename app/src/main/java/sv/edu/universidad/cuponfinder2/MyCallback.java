package sv.edu.universidad.cuponfinder2;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import sv.edu.universidad.cuponfinder2.Model.Promocion;

public class MyCallback extends DiffUtil.Callback {
    private final List<Promocion> oldPromociones;
    private final List<Promocion> newPromociones;

    public MyCallback(List<Promocion> oldPromociones, List<Promocion> newPromociones) {
        this.oldPromociones = oldPromociones;
        this.newPromociones = newPromociones;
    }

    @Override
    public int getOldListSize() {
        return oldPromociones.size();
    }

    @Override
    public int getNewListSize() {
        return newPromociones.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPromociones.get(oldItemPosition).getIdPromo().equals(newPromociones.get(newItemPosition).getIdPromo());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPromociones.get(oldItemPosition).equals(newPromociones.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Aquí puedes devolver un objeto que represente el cambio entre los dos elementos
        // Por ejemplo, podrías devolver un objeto que contenga los campos que han cambiado
        // Esto te permitirá realizar animaciones específicas para cada cambio en tu RecyclerView
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
