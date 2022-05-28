package com.example.stopwaitingadmin.viewholder;

import com.example.stopwaitingadmin.R;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.activity.WaitingListActivity;
import com.example.stopwaitingadmin.adapter.AdminWaitingListAdapter;

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
            WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");

            //승인된 항목 개설된 가게 DB로 이동 필요( 승인 대기중 DB에서 삭제 및 개설된 가게 DB에 등록)

            adapter.notifyItemRemoved(getAdapterPosition());
        });
        btnReject = a_view.findViewById(R.id.btnFalse);
        btnReject.setOnClickListener(view -> {
            adapter.mItemList.remove(getAdapterPosition());
            WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");

            //거부된 항목 승인 대기중 DB에서 삭제 필요

            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public AdminWaitingListItemViewHolder linkAdapter(AdminWaitingListAdapter adapter) {
        this.adapter = adapter;
        return this;
    }
}
