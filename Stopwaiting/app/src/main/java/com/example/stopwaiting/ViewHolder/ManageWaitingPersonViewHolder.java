package com.example.stopwaiting.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.Adapter.ManageWaitingPersonAdapter;
import com.example.stopwaiting.R;

public class ManageWaitingPersonViewHolder extends RecyclerView.ViewHolder {
    public TextView txtName, txtCnt;
    public ManageWaitingPersonAdapter adapter;

    public ManageWaitingPersonViewHolder(View view, ManageWaitingPersonAdapter.OnItemLongClickEventListener mItemLongClickListener) {
        super(view);

        txtName = view.findViewById(R.id.txtName);
        txtCnt = view.findViewById(R.id.txtCnt);

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

    public ManageWaitingPersonViewHolder linkAdapter(ManageWaitingPersonAdapter adapter) {
        this.adapter = adapter;

        return this;
    }

}
