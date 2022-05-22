package com.example.stopwaitingadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.R;
import com.example.stopwaitingadmin.dto.ReportedUserListItem;
import com.example.stopwaitingadmin.viewholder.ReportedUserListItemViewHolder;

import java.util.List;

public class ReportedUserListAdapter extends RecyclerView.Adapter<ReportedUserListItemViewHolder> {
    Context mContext;
    public List<ReportedUserListItem> mItemList;

    public ReportedUserListAdapter(Context a_context, List<ReportedUserListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    @Override
    public ReportedUserListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reported_user_list_item, parent, false);
        return new ReportedUserListItemViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedUserListItemViewHolder holder, int position) {
        final ReportedUserListItem reportedUserItem = mItemList.get(position);
        holder.txtName.setText("학번 : " + reportedUserItem.getReportedUserNum());
        holder.txtUser.setText("이름 : " + reportedUserItem.getReportedUserName());
        holder.txtLocation.setText("신고된 횟수 : " + reportedUserItem.getReportedCount());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}