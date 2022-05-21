package com.example.stopwaitingadmin;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AdminWaitingListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtUser, txtLocation;
    public Button btnAccept, btnReject;
    public AdminWaitingListAdapter adapter;

    public AdminWaitingListItemViewHolder(View a_view) {
        super(a_view);
        imgItem = a_view.findViewById(R.id.imgItem);
        txtName = a_view.findViewById(R.id.txtId);
        txtUser = a_view.findViewById(R.id.txtUser);
        txtLocation = a_view.findViewById(R.id.txtNum);
        btnAccept = a_view.findViewById(R.id.btnTrue);
        btnAccept.setOnClickListener(view -> {
            adapter.mItemList.remove(getAdapterPosition());
            AdminActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
            adapter.notifyItemRemoved(getAdapterPosition());
        });
        btnReject = a_view.findViewById(R.id.btnFalse);
        btnReject.setOnClickListener(view -> {
            adapter.mItemList.remove(getAdapterPosition());
            AdminActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public AdminWaitingListItemViewHolder linkAdapter(AdminWaitingListAdapter adapter) {
        this.adapter = adapter;
        return this;
    }
}
