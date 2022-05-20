package com.example.stopwaiting.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.ManageWaitingListAdapter;
import com.example.stopwaiting.R;

public class ManageWaitingListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtLocDetail, txtWaitCnt;
    public ManageWaitingListAdapter adapter;

    public ManageWaitingListItemViewHolder(View view, ManageWaitingListAdapter.OnItemClickEventListener mItemClickListener,
                                           ManageWaitingListAdapter.OnItemLongClickEventListener mItemLongClickListener) {
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
            public boolean onLongClick(View v) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mItemLongClickListener.onItemLongClick(position);
                    return true;
                }

                return false;
            }
        });
    }

    public ManageWaitingListItemViewHolder linkAdapter(ManageWaitingListAdapter adapter) {
        this.adapter = adapter;

        return this;
    }

}
