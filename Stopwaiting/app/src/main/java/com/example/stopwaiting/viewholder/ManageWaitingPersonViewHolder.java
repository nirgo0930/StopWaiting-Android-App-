package com.example.stopwaiting.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.ManageWaitingPersonAdapter;
import com.example.stopwaiting.R;

public class ManageWaitingPersonViewHolder extends RecyclerView.ViewHolder {
    public TextView txtName, txtCnt, txtTelNum, txtSCode;
    public ManageWaitingPersonAdapter adapter;

    public ManageWaitingPersonViewHolder(View view, ManageWaitingPersonAdapter.OnItemLongClickEventListener mItemLongClickListener) {
        super(view);

        txtName = view.findViewById(R.id.txtName);
        txtCnt = view.findViewById(R.id.txtCnt);
        txtTelNum = view.findViewById(R.id.txtTelNum);
        txtSCode = view.findViewById(R.id.txtStudentCode);

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
