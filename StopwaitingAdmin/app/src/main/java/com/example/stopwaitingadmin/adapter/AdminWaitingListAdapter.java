package com.example.stopwaitingadmin.adapter;
import com.example.stopwaitingadmin.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.activity.DataApplication;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;
import com.example.stopwaitingadmin.viewholder.AdminWaitingListItemViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdminWaitingListAdapter extends RecyclerView.Adapter<AdminWaitingListItemViewHolder> {
    Context mContext;
    public List<AdminWaitingListItem> mItemList;

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
        holder.txtName.setText(waitingItem.getTxtName());
        holder.txtUser.setText("신청자 : " + waitingItem.getTxtUser());
        holder.txtLocation.setText(waitingItem.getTxtLocation());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

//    public void permissionResultRequest(Boolean TorF){
//        JSONObject jsonBodyObj = new JSONObject();
//        try {
//            jsonBodyObj.put("WaitingId",mItemList.get(pos) );
//            jsonBodyObj.put("PermissionResult", TorF);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        final String requestBody = String.valueOf(jsonBodyObj.toString());
//    }
}