package com.example.stopwaiting.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.ShowListAdapter;

public class ShowListViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtLocDetail, txtWaitCnt;
    public ShowListAdapter adapter;

    public ShowListViewHolder(View view, ShowListAdapter.OnItemClickEventListener mItemClickListener) {
        super(view);
        imgItem = view.findViewById(R.id.imgItem);
        txtName = view.findViewById(R.id.txtName);
        txtLocDetail = view.findViewById(R.id.txtLocDetail);
        txtWaitCnt = view.findViewById(R.id.txtWaitCnt);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });

    }

    public ShowListViewHolder linkAdapter(ShowListAdapter adapter) {
        this.adapter = adapter;

        return this;
    }

}