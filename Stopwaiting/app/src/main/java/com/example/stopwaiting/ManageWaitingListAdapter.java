package com.example.stopwaiting;

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

import java.util.List;

public class ManageWaitingListAdapter extends RecyclerView.Adapter<ManageWaitingListItemViewHolder> {
    private Context mContext;
    private List<WaitingListItem> mItemList;

    public ManageWaitingListAdapter(Context a_context, List<WaitingListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    public interface OnItemClickEventListener {
        void onItemClick(String name);
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(int pos);
    }

    private ManageWaitingListAdapter.OnItemClickEventListener mItemClickListener = new ManageWaitingListAdapter.OnItemClickEventListener() {
        @Override
        public void onItemClick(String name) {
            Intent data = new Intent(mContext, ManageWaitingActivity.class);
            data.putExtra("name", name);
            mContext.startActivity(data);
        }
    };

    private ManageWaitingListAdapter.OnItemLongClickEventListener mItemLongClickListener = new ManageWaitingListAdapter.OnItemLongClickEventListener() {
        @Override
        public void onItemLongClick(int pos) {
            new AlertDialog.Builder(mContext)
                    .setTitle(mItemList.get(pos).getName())
                    .setMessage("해당 웨이팅을 종료하시겠습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            String name = mItemList.get(pos).getName();
                            mItemList.remove(pos);

                            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                                if (DataApplication.testDBList.get(i).getName().equals(name)) {
                                    DataApplication.testDBList.remove(i);
                                    break;
                                }
                            }
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
    public ManageWaitingListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.waiting_item, parent, false);
        return new ManageWaitingListItemViewHolder(view, mItemClickListener, mItemLongClickListener).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull ManageWaitingListItemViewHolder holder, int position) {
        final WaitingListItem waitingItem = mItemList.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(waitingItem.getImgUri())
                .into(holder.imgItem);

        holder.txtName.setText(waitingItem.getName());
        holder.txtLocDetail.setText(waitingItem.getLocDetail());
        int cnt = waitingItem.getWaitingCnt();
        if (cnt < 1) cnt = 0;
        holder.txtWaitCnt.setText("대기중인 인원 :  " + String.valueOf(cnt) + " 명");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}