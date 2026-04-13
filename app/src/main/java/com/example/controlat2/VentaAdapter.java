package com.example.controlat2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private List<Venta> listaVentas;
    private int posicionSeleccionada = -1;

    public VentaAdapter(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    public Venta getVentaSeleccionada() {
        if (posicionSeleccionada != -1) {
            return listaVentas.get(posicionSeleccionada);
        }
        return null;
    }

    public void limpiarSeleccion() {
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombreProducto, txtNombreCliente, txtCantidad, txtTotal, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }

    @NonNull
    @Override
    public VentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venta, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaAdapter.ViewHolder holder, int position) {
        Venta venta = listaVentas.get(position);

        holder.txtNombreProducto.setText(venta.getNombreProducto());
        holder.txtNombreCliente.setText("Cliente: " + venta.getNombreCliente());
        holder.txtCantidad.setText("Cantidad: " + venta.getCantidad());
        holder.txtTotal.setText("Total: $" + venta.getTotal());
        holder.txtFecha.setText("Fecha: " + venta.getFecha());

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
        return listaVentas.size();
    }
}