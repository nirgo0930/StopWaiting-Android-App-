package com.example.stopwaiting.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stopwaiting.R;
import com.example.stopwaiting.activity.CheckMyWaitingActivity;
import com.example.stopwaiting.activity.MyPageActivity;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.viewholder.ShowListViewHolder;

import java.util.List;

public class ShowListAdapter extends RecyclerView.Adapter<ShowListViewHolder> {
    private Context mContext;
    private List<WaitingInfo> mItemList;
    private int myNum = 0;

    public ShowListAdapter(Context a_context, List<WaitingInfo> a_itemList) {
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
            data.putExtra("case", 1);

            MyPageActivity.myPageActivity.setResult(Activity.RESULT_OK, data);
            MyPageActivity.myPageActivity.finish();
            CheckMyWaitingActivity.myWaitingActivity.finish();
        }
    };

    @NonNull
    @Override
    public ShowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.waiting_item, parent, false);
        return new ShowListViewHolder(view, mItemClickListener).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowListViewHolder holder, int position) {
        final WaitingInfo waitingItem = mItemList.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(waitingItem.getUrlList().get(0))
                .into(holder.imgItem);

        holder.txtName.setText(waitingItem.getName());
        holder.txtLocDetail.setText(waitingItem.getLocDetail());

        if (myNum < 1) myNum = 0;
        holder.txtWaitCnt.setText("대기중인 인원 :  " + String.valueOf(myNum) + " 명");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void myNumRequest() {
//        waitingItem.getQueueList().get(0).getWaitingPersonList().indexOf(DataApplication.currentUser)
    }
}
