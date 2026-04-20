package com.example.controlat2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VentaHistorialAdapter extends RecyclerView.Adapter<VentaHistorialAdapter.ViewHolder> {

    private List<Venta> listaVentas;

    public VentaHistorialAdapter(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_venta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta venta = listaVentas.get(position);

        holder.txtProducto.setText(venta.getNombreProducto());
        holder.txtCantidad.setText("Cantidad: " + venta.getCantidad());
        holder.txtTotal.setText("Total: $" + String.format("%.2f", venta.getTotal()));
        holder.txtFecha.setText("Fecha: " + venta.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaVentas != null ? listaVentas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProducto, txtCantidad, txtTotal, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProducto = itemView.findViewById(R.id.txtProducto);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}