package com.example.stopwaiting.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.ManageWaitingPersonActivity;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.R;
import com.example.stopwaiting.viewholder.ManageWaitingPersonViewHolder;

import java.util.ArrayList;

public class ManageWaitingPersonAdapter extends RecyclerView.Adapter<ManageWaitingPersonViewHolder> {
    private Context mContext;
    private ArrayList<UserInfo> mItemList;

    public ManageWaitingPersonAdapter(Context a_context, ArrayList<UserInfo> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(int pos);
    }

    private ManageWaitingPersonAdapter.OnItemLongClickEventListener mItemLongClickListener = new ManageWaitingPersonAdapter.OnItemLongClickEventListener() {
        @Override
        public void onItemLongClick(int pos) {
            new AlertDialog.Builder(mContext)
                    .setTitle("재확인")
                    .setMessage(mItemList.get(pos).getName() + " 님을 명단에서 제외하시겟습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            UserInfo userInfo = mItemList.get(pos);
                            mItemList.remove(pos);

                            if (DataApplication.isTest) { //server 요청
                                for (int i = 0; i < DataApplication.testWaitingQueueDBList.size(); i++) {
                                    if (DataApplication.testWaitingQueueDBList.get(i).getQId().equals(ManageWaitingPersonActivity.qId)) {
                                        WaitingQueue temp = DataApplication.testWaitingQueueDBList.get(i);
                                        temp.removeWPerson(userInfo);

                                        DataApplication.testWaitingQueueDBList.set(i, temp);
                                        break;
                                    }
                                }
                            } else {
                                
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
    public ManageWaitingPersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.manage_waiting_person_item, parent, false);
        return new ManageWaitingPersonViewHolder(view, mItemLongClickListener).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull ManageWaitingPersonViewHolder holder, int position) {
        final UserInfo waitingPerson = mItemList.get(position);

        holder.txtName.setText(waitingPerson.getName());
        holder.txtCnt.setText(String.valueOf(position + 1) + ".");
        holder.txtTelNum.setText(waitingPerson.getTel());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}