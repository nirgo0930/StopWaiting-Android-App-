package com.example.stopwaiting.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stopwaiting.activity.CheckMyWaitingActivity;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.MyPageActivity;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.viewholder.MyWaitingListItemViewHolder;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.WaitingListItem;

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

    public interface OnItemLongClickEventListener {
        void onItemLongClick(int pos);
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

    private OnItemLongClickEventListener mItemLongClickListener = new OnItemLongClickEventListener() {
        @Override
        public void onItemLongClick(int pos) {
            new AlertDialog.Builder(mContext)
                    .setTitle(mItemList.get(pos).getName())
                    .setMessage("해당 웨이팅을 취소하시겠습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            String name = mItemList.get(pos).getName();
                            mItemList.remove(pos);

                            Intent data = new Intent();
                            data.putExtra("name", name);
                            data.putExtra("case", 2);
                            MyPageActivity.myPageActivity.setResult(Activity.RESULT_OK, data);
                            MyPageActivity.myPageActivity.finish();
                            CheckMyWaitingActivity.myWaitingActivity.finish();

                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 취소시 처리 로직

                        }
                    })
                    .show();
        }
    };

    @Override
    public MyWaitingListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.waiting_item, parent, false);
        return new MyWaitingListItemViewHolder(view, mItemClickListener, mItemLongClickListener).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWaitingListItemViewHolder holder, int position) {
        final WaitingListItem waitingItem = mItemList.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(waitingItem.getImgUri())
                .into(holder.imgItem);

        holder.txtName.setText(waitingItem.getName());
        holder.txtLocDetail.setText(waitingItem.getLocDetail());
        holder.txtWaitCnt.setText("대기중인 인원 :  " + (waitingItem.getWaitingCnt() + 1) + " 명");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}