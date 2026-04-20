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

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Pedido> listaPedidos;
    private int posicionSeleccionada = -1;

    public PedidoAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public Pedido getPedidoSeleccionado() {
        if (posicionSeleccionada >= 0 && posicionSeleccionada < listaPedidos.size()) {
            return listaPedidos.get(posicionSeleccionada);
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

    public void actualizarLista(List<Pedido> nuevaLista) {
        this.listaPedidos = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardPedido;
        TextView txtPedidoCliente, txtPedidoProducto, txtPedidoCantidad, txtPedidoFecha, txtPedidoEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardPedido = (CardView) itemView;
            txtPedidoCliente = itemView.findViewById(R.id.txtPedidoCliente);
            txtPedidoProducto = itemView.findViewById(R.id.txtPedidoProducto);
            txtPedidoCantidad = itemView.findViewById(R.id.txtPedidoCantidad);
            txtPedidoFecha = itemView.findViewById(R.id.txtPedidoFecha);
            txtPedidoEstado = itemView.findViewById(R.id.txtPedidoEstado);
        }
    }

    @NonNull
    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);

        holder.txtPedidoCliente.setText("Cliente: " + pedido.getNombreCliente());
        holder.txtPedidoProducto.setText("Producto: " + pedido.getNombreProducto());
        holder.txtPedidoCantidad.setText("Cantidad: " + pedido.getCantidad());
        holder.txtPedidoFecha.setText("Entrega: " + pedido.getFechaEntrega());
        holder.txtPedidoEstado.setText("Estado: " + pedido.getEstado());

        // COLOR DEL ESTADO
        String estado = pedido.getEstado() != null ? pedido.getEstado() : "";

        if (estado.equalsIgnoreCase("Pendiente")) {
            holder.txtPedidoEstado.setTextColor(Color.parseColor("#FF9800"));
        } else if (estado.equalsIgnoreCase("Entregado")) {
            holder.txtPedidoEstado.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.txtPedidoEstado.setTextColor(Color.parseColor("#F44336"));
        }

        // SELECCIÓN VISUAL
        if (position == posicionSeleccionada) {
            holder.cardPedido.setCardBackgroundColor(Color.parseColor("#2A3F66"));
        } else {
            holder.cardPedido.setCardBackgroundColor(Color.parseColor("#1B263B"));
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
        return listaPedidos != null ? listaPedidos.size() : 0;
    }
}