package com.example.controlat2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    public interface OnClienteMenuClickListener {
        void onVerHistorial(Cliente cliente);
    }

    private List<Cliente> listaClientes;
    private int posicionSeleccionada = -1;
    private final OnClienteMenuClickListener listener;

    public ClienteAdapter(List<Cliente> listaClientes, OnClienteMenuClickListener listener) {
        this.listaClientes = listaClientes;
        this.listener = listener;
    }

    public Cliente getClienteSeleccionado() {
        if (posicionSeleccionada >= 0 && posicionSeleccionada < listaClientes.size()) {
            return listaClientes.get(posicionSeleccionada);
        }
        return null;
    }

    public void limpiarSeleccion() {
        int posicionAnterior = posicionSeleccionada;
        posicionSeleccionada = -1;
        if (posicionAnterior != -1) {
            notifyItemChanged(posicionAnterior);
        }
    }

    public void actualizarLista(List<Cliente> nuevaLista) {
        this.listaClientes = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public void setListaClientes(List<Cliente> nuevaLista) {
        this.listaClientes = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);

        holder.txtNombreCliente.setText(cliente.getNombre());
        holder.txtTelefonoCliente.setText("Teléfono: " + cliente.getTelefono());
        holder.txtCorreoCliente.setText("Correo: " + cliente.getCorreo());
        holder.txtNotasCliente.setText("Notas: " + cliente.getNotas());

        if (position == posicionSeleccionada) {
            holder.cardCliente.setCardBackgroundColor(Color.parseColor("#2A3F66"));
        } else {
            holder.cardCliente.setCardBackgroundColor(Color.parseColor("#1B263B"));
        }

        holder.itemView.setOnClickListener(v -> {
            int posicionAnterior = posicionSeleccionada;
            int posicionActual = holder.getAdapterPosition();

            if (posicionActual == RecyclerView.NO_POSITION) {
                return;
            }

            if (posicionActual == posicionSeleccionada) {
                posicionSeleccionada = -1; // deseleccionar si toca la misma
            } else {
                posicionSeleccionada = posicionActual;
            }

            if (posicionAnterior != -1) {
                notifyItemChanged(posicionAnterior);
            }
            if (posicionSeleccionada != -1) {
                notifyItemChanged(posicionSeleccionada);
            }
        });

        holder.btnOpcionesCliente.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.btnOpcionesCliente);
            popupMenu.getMenuInflater().inflate(R.menu.menu_opciones_cliente, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opcion_ver_historial) {
                    if (listener != null) {
                        listener.onVerHistorial(cliente);
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes != null ? listaClientes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardCliente;
        TextView txtNombreCliente;
        TextView txtTelefonoCliente;
        TextView txtCorreoCliente;
        TextView txtNotasCliente;
        TextView btnOpcionesCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCliente = (CardView) itemView;
            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            txtTelefonoCliente = itemView.findViewById(R.id.txtTelefonoCliente);
            txtCorreoCliente = itemView.findViewById(R.id.txtCorreoCliente);
            txtNotasCliente = itemView.findViewById(R.id.txtNotasCliente);
            btnOpcionesCliente = itemView.findViewById(R.id.btnOpcionesCliente);
        }
    }
}