package com.encanto_belleza_app.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.model.Servicio;
import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {
    private List<Servicio> servicios;
    private OnAgendarClickListener agendarClickListener;

    public interface OnAgendarClickListener {
        void onAgendarClick(Servicio servicio);
    }

    public ServicioAdapter(List<Servicio> servicios, OnAgendarClickListener listener) {
        this.servicios = servicios;
        this.agendarClickListener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio);

        holder.btnAgendar.setOnClickListener(v -> {
            if (agendarClickListener != null) {
                agendarClickListener.onAgendarClick(servicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicios != null ? servicios.size() : 0;
    }

    public void updateServicios(List<Servicio> nuevosServicios) {
        servicios = nuevosServicios;
        notifyDataSetChanged();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreServicio, tvDescripcion, tvDuracion, tvPrecio;
        Button btnAgendar;

        ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreServicio = itemView.findViewById(R.id.tvNombreServicio);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgendar = itemView.findViewById(R.id.btnAgendar);
        }

        void bind(Servicio servicio) {
            tvNombreServicio.setText(servicio.getNombre());
            tvDescripcion.setText(servicio.getDescripcion());
            tvDuracion.setText("Duración: " + servicio.getDuracion() + " min");
            tvPrecio.setText(String.format("$%.2f", servicio.getPrecio()));
        }
    }
}
