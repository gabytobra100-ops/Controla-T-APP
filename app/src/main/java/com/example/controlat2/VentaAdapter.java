package com.example.controlat2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private List<Venta> listaVentas;
    private int posicionSeleccionada = -1;

    public VentaAdapter(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    public Venta getVentaSeleccionada() {
        if (posicionSeleccionada >= 0 && posicionSeleccionada < listaVentas.size()) {
            return listaVentas.get(posicionSeleccionada);
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

    public void actualizarLista(List<Venta> nuevaLista) {
        this.listaVentas = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardVenta;
        TextView txtNombreProducto, txtNombreCliente, txtCantidad, txtTotal, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardVenta = (CardView) itemView;
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
        holder.txtTotal.setText("Total: $" + String.format("%.2f", venta.getTotal()));
        holder.txtFecha.setText("Fecha: " + venta.getFecha());

        if (position == posicionSeleccionada) {
            holder.cardVenta.setCardBackgroundColor(Color.parseColor("#2A3F66"));
        } else {
            holder.cardVenta.setCardBackgroundColor(Color.parseColor("#1B263B"));
        }

        holder.itemView.setOnClickListener(v -> {
            int posicionAnterior = posicionSeleccionada;
            int posicionActual = holder.getAdapterPosition();

            if (posicionActual == RecyclerView.NO_POSITION) {
                return;
            }

            if (posicionActual == posicionSeleccionada) {
                posicionSeleccionada = -1;
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
    }

    @Override
    public int getItemCount() {
        return listaVentas != null ? listaVentas.size() : 0;
    }
}