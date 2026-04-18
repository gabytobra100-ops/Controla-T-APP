package com.example.controlat2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> listaProductos;
    private AppDatabase db;
    private int posicionSeleccionada = -1;

    public ProductoAdapter(List<Producto> listaProductos, AppDatabase db) {
        this.listaProductos = listaProductos;
        this.db = db;
    }

    public Producto getProductoSeleccionado() {
        if (posicionSeleccionada != -1) {
            return listaProductos.get(posicionSeleccionada);
        }
        return null;
    }

    public int getPosicionSeleccionada() {
        return posicionSeleccionada;
    }

    public void limpiarSeleccion() {
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto;
        TextView nombre, descripcion, precio, stock, id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProducto);
            nombre = itemView.findViewById(R.id.txtNombreProducto);
            descripcion = itemView.findViewById(R.id.txtDescripcionProducto);
            precio = itemView.findViewById(R.id.txtPrecioProducto);
            stock = itemView.findViewById(R.id.txtStockProducto);
            id = itemView.findViewById(R.id.txtIdProducto);
        }
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = listaProductos.get(position);

        holder.imgProducto.setImageResource(p.getImagenResId());
        holder.nombre.setText(p.getNombre());
        holder.descripcion.setText(p.getDescripcion());
        holder.precio.setText("Precio: $" + p.getPrecio());
        holder.stock.setText("Stock: " + p.getStock());
        holder.id.setText("ID: " + p.getId());

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
        return listaProductos.size();
    }
}