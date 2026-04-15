package com.example.controlat2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Pedido> listaPedidos;
    private AppDatabase db;

    public PedidoAdapter(List<Pedido> listaPedidos, AppDatabase db) {
        this.listaPedidos = listaPedidos;
        this.db = db;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCliente, txtProducto, txtCantidad, txtFecha, txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCliente = itemView.findViewById(R.id.txtPedidoCliente);
            txtProducto = itemView.findViewById(R.id.txtPedidoProducto);
            txtCantidad = itemView.findViewById(R.id.txtPedidoCantidad);
            txtFecha = itemView.findViewById(R.id.txtPedidoFecha);
            txtEstado = itemView.findViewById(R.id.txtPedidoEstado);
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

        holder.txtCliente.setText("Cliente: " + pedido.getNombreCliente());
        holder.txtProducto.setText("Producto: " + pedido.getNombreProducto());
        holder.txtCantidad.setText("Cantidad: " + pedido.getCantidad());
        holder.txtFecha.setText("Entrega: " + pedido.getFechaEntrega());

        if (pedido.getEstado().equals("Entregado")) {
            holder.txtEstado.setText("Estado: Entregado");
            holder.txtEstado.setTextColor(0xFF4CAF50);
        } else {
            holder.txtEstado.setText("Estado: Pendiente");
            holder.txtEstado.setTextColor(0xFFFF9800);
        }

        holder.itemView.setOnClickListener(v -> {
            if (pedido.getEstado().equals("Pendiente")) {

                Producto producto = db.productoDao().obtenerPorNombre(pedido.getNombreProducto());

                if (producto == null) {
                    Toast.makeText(v.getContext(), "No se encontró el producto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (producto.getStock() < pedido.getCantidad()) {
                    Toast.makeText(v.getContext(), "No hay suficiente stock para convertir el pedido", Toast.LENGTH_SHORT).show();
                    return;
                }

                int clienteId = 0;
                Cliente cliente = db.clienteDao().obtenerPorNombre(pedido.getNombreCliente());
                if (cliente != null) {
                    clienteId = cliente.getId();
                }

                double total = producto.getPrecio() * pedido.getCantidad();

                Venta venta = new Venta(
                        clienteId,
                        pedido.getNombreCliente(),
                        pedido.getNombreProducto(),
                        pedido.getCantidad(),
                        total,
                        pedido.getFechaEntrega()
                );

                db.ventaDao().insertar(venta);

                producto.setStock(producto.getStock() - pedido.getCantidad());
                db.productoDao().actualizar(producto);

                pedido.setEstado("Entregado");
                db.pedidoDao().actualizar(pedido);

                notifyItemChanged(position);

                Toast.makeText(v.getContext(), "Pedido convertido en venta", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(v.getContext(), "Este pedido ya fue convertido", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }
}