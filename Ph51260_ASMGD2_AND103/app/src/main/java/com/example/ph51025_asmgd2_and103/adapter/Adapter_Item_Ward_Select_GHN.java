package com.example.ph51025_asmgd2_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ph51025_asmgd2_and103.R;
import com.example.ph51025_asmgd2_and103.model.Ward;

import java.util.ArrayList;

public class Adapter_Item_Ward_Select_GHN extends BaseAdapter {
    Context context;
    ArrayList<Ward> list;

    // Constructor để truyền vào context và danh sách các phường/xã
    public Adapter_Item_Ward_Select_GHN(Context context, ArrayList<Ward> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();  // Số lượng item trong danh sách
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);  // Trả về item tại vị trí cụ thể
    }

    @Override
    public long getItemId(int position) {
        return position;  // Trả về ID của item (thường dùng position)
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo một view mới cho mỗi item trong Spinner
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_district_spinner, parent, false);
        }

        // Lấy đối tượng Ward tại vị trí position
        Ward ward = list.get(position);

        // Ánh xạ TextView trong layout item_district_spinner
        TextView tvWard = convertView.findViewById(R.id.textViewDistrictName);

        // Hiển thị tên phường/xã trong TextView
        tvWard.setText(ward.getWardName());

        // Trả về view đã được cập nhật
        return convertView;
    }
}
