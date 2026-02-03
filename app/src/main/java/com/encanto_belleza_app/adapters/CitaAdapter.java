package com.encanto_belleza_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.encanto_belleza_app.R;
import com.encanto_belleza_app.controller.CitaController;
import com.encanto_belleza_app.controller.ServicioController;
import com.encanto_belleza_app.controller.UsuarioController;
import com.encanto_belleza_app.model.Cita;
import com.encanto_belleza_app.model.Servicio;
import com.encanto_belleza_app.model.Usuario;
import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {
    private List<Cita> citas;
    private Context context;
    private CitaController citaController;
    private ServicioController servicioController;
    private UsuarioController usuarioController;
    private OnCitaActionListener actionListener;

    public interface OnCitaActionListener {
        void onCitaUpdated();
        void onCitaDeleted();
    }

    public CitaAdapter(Context context, List<Cita> citas, OnCitaActionListener listener) {
        this.context = context;
        this.citas = citas;
        this.actionListener = listener;
        this.citaController = new CitaController(context);
        this.servicioController = new ServicioController(context);
        this.usuarioController = new UsuarioController(context);
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita_agenda, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = citas.get(position);
        holder.bind(cita);

        // Configurar botones
        holder.btnCancelarCita.setOnClickListener(v -> {
            cancelarCita(cita.getId());
        });

        holder.btnEditarCita.setOnClickListener(v -> {
            Toast.makeText(context, "Funcionalidad de reprogramar en desarrollo", Toast.LENGTH_SHORT).show();
        });

        holder.btnMarcarPagada.setOnClickListener(v -> {
            marcarComoPagada(cita);
        });

        // Configurar clic en toda la card
        holder.itemView.setOnClickListener(v -> {
            mostrarDetalleCita(cita);
        });
    }

    @Override
    public int getItemCount() {
        return citas != null ? citas.size() : 0;
    }

    public void updateCitas(List<Cita> nuevasCitas) {
        citas = nuevasCitas;
        notifyDataSetChanged();
    }

    private void cancelarCita(int idCita) {
        boolean exito = citaController.cancelarCita(idCita);
        if (exito) {
            Toast.makeText(context, "Cita cancelada exitosamente", Toast.LENGTH_SHORT).show();
            if (actionListener != null) {
                actionListener.onCitaUpdated();
            }
        } else {
            Toast.makeText(context, "Error al cancelar la cita", Toast.LENGTH_SHORT).show();
        }
    }

    private void marcarComoPagada(Cita cita) {
        cita.setPagada(true);
        boolean exito = citaController.actualizarCita(cita);
        if (exito) {
            Toast.makeText(context, "Cita marcada como pagada", Toast.LENGTH_SHORT).show();
            if (actionListener != null) {
                actionListener.onCitaUpdated();
            }
        } else {
            Toast.makeText(context, "Error al marcar como pagada", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDetalleCita(Cita cita) {
        // Obtener información completa
        Servicio servicio = servicioController.obtenerServicioPorId(cita.getIdServicio());
        Usuario empleado = usuarioController.obtenerUsuarioPorId(cita.getIdEmpleado());

        String mensaje = "Detalle de Cita:\n" +
                "Servicio: " + (servicio != null ? servicio.getNombre() : "N/A") + "\n" +
                "Empleado: " + (empleado != null ? empleado.getNombre() : "N/A") + "\n" +
                "Fecha: " + citaController.formatearFechaParaMostrar(cita.getFecha()) + "\n" +
                "Hora: " + citaController.formatearHoraParaMostrar(cita.getHora()) + "\n" +
                "Estado: " + cita.getEstado() + "\n" +
                "Pagada: " + (cita.isPagada() ? "Sí" : "No");

        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvEstado, tvPagada, tvIdCita, tvServicio, tvFecha, tvHora, tvEmpleado, tvDuracion, tvPrecio;
        Button btnCancelarCita, btnEditarCita, btnMarcarPagada;

        CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvPagada = itemView.findViewById(R.id.tvPagada);
            tvIdCita = itemView.findViewById(R.id.tvIdCita);
            tvServicio = itemView.findViewById(R.id.tvServicio);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEmpleado = itemView.findViewById(R.id.tvEmpleado);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnCancelarCita = itemView.findViewById(R.id.btnCancelarCita);
            btnEditarCita = itemView.findViewById(R.id.btnEditarCita);
            btnMarcarPagada = itemView.findViewById(R.id.btnMarcarPagada);
        }

        void bind(Cita cita) {
            // Estado
            tvEstado.setText(cita.getEstado().toUpperCase());
            switch (cita.getEstado()) {
                case "confirmada":
                    tvEstado.setBackgroundResource(R.drawable.bg_estado_confirmada);
                    break;
                case "pendiente":
                    tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente);
                    break;
                case "cancelada":
                    tvEstado.setBackgroundResource(R.drawable.bg_estado_cancelada);
                    break;
            }

            // Pagada
            if (cita.isPagada()) {
                tvPagada.setVisibility(View.VISIBLE);
            } else {
                tvPagada.setVisibility(View.GONE);
            }

            // ID
            tvIdCita.setText("#" + String.format("%03d", cita.getId()));

            // Estos campos se llenarán desde la Activity con datos completos
            // Por ahora mostramos placeholder
        }
    }
}