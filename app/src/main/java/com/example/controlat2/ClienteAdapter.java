package com.example.controlat2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private List<Cliente> listaClientes;
    private int posicionSeleccionada = -1;

    public ClienteAdapter(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Cliente getClienteSeleccionado() {
        if (posicionSeleccionada != -1) {
            return listaClientes.get(posicionSeleccionada);
        }
        return null;
    }
    public void limpiarSeleccion() {
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }
    public void actualizarLista(List<Cliente>nuevaLista){
        this.listaClientes = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombreCliente, txtTelefonoCliente, txtCorreoCliente, txtNotasCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            txtTelefonoCliente = itemView.findViewById(R.id.txtTelefonoCliente);
            txtCorreoCliente = itemView.findViewById(R.id.txtCorreoCliente);
            txtNotasCliente = itemView.findViewById(R.id.txtNotasCliente);
        }
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);

        holder.txtNombreCliente.setText(cliente.getNombre());
        holder.txtTelefonoCliente.setText("Teléfono: " + cliente.getTelefono());
        holder.txtCorreoCliente.setText("Correo: " + cliente.getCorreo());
        holder.txtNotasCliente.setText("Notas: " + cliente.getNotas());

        if (position == posicionSeleccionada) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            posicionSeleccionada = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }
}