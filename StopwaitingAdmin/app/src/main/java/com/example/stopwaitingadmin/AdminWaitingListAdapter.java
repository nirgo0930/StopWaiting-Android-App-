package com.example.stopwaitingadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminWaitingListAdapter extends RecyclerView.Adapter<AdminWaitingListItemViewHolder> {
    Context mContext;
    List<AdminWaitingListItem> mItemList;

    public AdminWaitingListAdapter(Context a_context, List<AdminWaitingListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    @Override
    public AdminWaitingListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_wait_list_item, parent, false);
        return new AdminWaitingListItemViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminWaitingListItemViewHolder holder, int position) {
        final AdminWaitingListItem waitingItem = mItemList.get(position);
        holder.imgItem.setImageResource(waitingItem.getImgId());
        holder.txtName.setText("신청자 : " + waitingItem.getTxtName());
        holder.txtUser.setText(waitingItem.getTxtUser());
        holder.txtLocation.setText(waitingItem.getTxtLocation());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}