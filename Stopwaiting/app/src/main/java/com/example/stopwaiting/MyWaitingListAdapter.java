package com.example.stopwaiting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyWaitingListAdapter extends RecyclerView.Adapter<MyWaitingListItemViewHolder> {
    private Context mContext;
    private List<WaitingListItem> mItemList;

    public MyWaitingListAdapter(Context a_context, List<WaitingListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    public interface OnItemClickEventListener {
        void onItemClick(String name);
    }

    private OnItemClickEventListener mItemClickListener = new OnItemClickEventListener() {
        @Override
        public void onItemClick(String name) {

            Intent data = new Intent();
            data.putExtra("name", name);
            MyPageActivity.myPageActivity.setResult(Activity.RESULT_OK, data);
            MyPageActivity.myPageActivity.finish();
            CheckMyWaitingActivity.myWaitingActivity.finish();
        }
    };

    @Override
    public MyWaitingListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.waiting_item, parent, false);
        return new MyWaitingListItemViewHolder(view, mItemClickListener).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull MyWaitingListItemViewHolder holder, int position) {
        final WaitingListItem waitingItem = mItemList.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(waitingItem.getImgUri())
                .into(holder.imgItem);

        holder.txtName.setText(waitingItem.getName());
        holder.txtLocDetail.setText(waitingItem.getLocDetail());
        holder.txtWaitCnt.setText("대기중인 인원 :  " + String.valueOf(waitingItem.getWaitingCnt()) + " 명");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}