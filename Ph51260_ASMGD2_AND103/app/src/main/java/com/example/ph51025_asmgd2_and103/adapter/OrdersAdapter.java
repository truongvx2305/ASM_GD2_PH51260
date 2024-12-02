package com.example.ph51025_asmgd2_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ph51025_asmgd2_and103.databinding.ItemOrdersBinding;
import com.example.ph51025_asmgd2_and103.model.Order;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Order> list;
    private OrderClick orderClick;

    public OrdersAdapter(Context context, ArrayList<Order> list, OrderClick orderClick) {
        this.context = context;
        this.list = list;
        this.orderClick = orderClick;
    }

    public interface OrderClick {
        void deleteOrder(String order_code);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrdersBinding binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order obj = list.get(position);
        holder.binding.tvOrderCode.setText(obj.getOrder_code());
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClick.deleteOrder(obj.getOrder_code());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrdersBinding binding;
        public ViewHolder(ItemOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
