package com.example.stopwaiting.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.SettingImageAdapter;
import com.example.stopwaiting.R;

public class SettingImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;

    public SettingImageViewHolder(View a_view, final SettingImageAdapter.OnItemClickEventListener a_itemClickListener) {
        super(a_view);
        imgItem = a_view.findViewById(R.id.imgItem);
        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    a_itemClickListener.onItemClick(position);
                }
            }
        });
    }
}
