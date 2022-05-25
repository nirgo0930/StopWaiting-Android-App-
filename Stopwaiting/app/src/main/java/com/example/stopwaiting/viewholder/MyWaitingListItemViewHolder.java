package com.example.stopwaiting.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.R;

public class MyWaitingListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtLocDetail, txtWaitCnt;
    public MyWaitingListAdapter adapter;

    public MyWaitingListItemViewHolder(View view, MyWaitingListAdapter.OnItemClickEventListener mItemClickListener,
                                       MyWaitingListAdapter.OnItemLongClickEventListener mItemLongClickListener) {
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
                    mItemClickListener.onItemClick(txtName.getText().toString());
                }

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mItemLongClickListener.onItemLongClick(position);
                }

                return false;
            }
        });
    }

    public MyWaitingListItemViewHolder linkAdapter(MyWaitingListAdapter adapter) {
        this.adapter = adapter;

        return this;
    }

}
