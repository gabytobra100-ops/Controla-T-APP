package com.example.controlat2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        if (posicionSeleccionada >= 0 && posicionSeleccionada < listaProductos.size()) {
            return listaProductos.get(posicionSeleccionada);
        }
        return null;
    }

    public int getPosicionSeleccionada() {
        return posicionSeleccionada;
    }

    public void limpiarSeleccion() {
        int posicionAnterior = posicionSeleccionada;
        posicionSeleccionada = -1;
        if (posicionAnterior != -1) {
            notifyItemChanged(posicionAnterior);
        }
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.listaProductos = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardProducto;
        ImageView imgProducto;
        TextView nombre, descripcion, precio, stock, id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProducto = (CardView) itemView;
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

        if (p.getImagenResId() != 0) {
            holder.imgProducto.setImageResource(p.getImagenResId());
        } else {
            holder.imgProducto.setImageResource(R.drawable.fragancias);
        }

        holder.nombre.setText(p.getNombre());
        holder.descripcion.setText(p.getDescripcion());
        holder.precio.setText("Precio: $" + String.format("%.2f", p.getPrecio()));
        holder.stock.setText("Stock: " + p.getStock());
        holder.id.setText("ID: " + p.getId());

        if (position == posicionSeleccionada) {
            holder.cardProducto.setCardBackgroundColor(Color.parseColor("#2A3F66"));
        } else {
            holder.cardProducto.setCardBackgroundColor(Color.parseColor("#1B263B"));
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
    }

    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;
    }
}