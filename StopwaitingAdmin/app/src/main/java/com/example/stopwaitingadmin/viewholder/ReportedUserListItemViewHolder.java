package com.example.stopwaitingadmin.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.R;
import com.example.stopwaitingadmin.activity.ReportedUserActivity;
import com.example.stopwaitingadmin.adapter.ReportedUserListAdapter;

public class ReportedUserListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtUser, txtLocation;
    public Button btnAccept, btnReject;
    public ReportedUserListAdapter adapter;

    public ReportedUserListItemViewHolder(View a_view) {
        super(a_view);
        imgItem = a_view.findViewById(R.id.imgItem);
        txtName = a_view.findViewById(R.id.txtId);
        txtUser = a_view.findViewById(R.id.txtUser);
        txtLocation = a_view.findViewById(R.id.txtNum);
        btnAccept = a_view.findViewById(R.id.btnTrue);
        btnAccept.setOnClickListener(view -> {
            adapter.mItemList.remove(getAdapterPosition());
            ReportedUserActivity.txtNotice.setText("신고된 회원이 " + String.valueOf(adapter.mItemList.size()) + "명 존재합니다.");
            adapter.notifyItemRemoved(getAdapterPosition());
        });
        btnReject = a_view.findViewById(R.id.btnFalse);
        btnReject.setOnClickListener(view -> {
            adapter.mItemList.remove(getAdapterPosition());
            ReportedUserActivity.txtNotice.setText("신고된 회원이 " + String.valueOf(adapter.mItemList.size()) + "명 존재합니다.");
            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public ReportedUserListItemViewHolder linkAdapter(ReportedUserListAdapter adapter) {
        this.adapter = adapter;
        return this;
    }
}
