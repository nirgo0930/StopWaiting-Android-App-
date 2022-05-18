package com.example.stopwaiting.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.Activity.DataApplication;
import com.example.stopwaiting.Activity.ManageWaitingPersonActivity;
import com.example.stopwaiting.DTO.WaitingQueue;
import com.example.stopwaiting.R;
import com.example.stopwaiting.ViewHolder.ManageWaitingPersonViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ManageWaitingPersonAdapter extends RecyclerView.Adapter<ManageWaitingPersonViewHolder> {
    private Context mContext;
    private ArrayList<String> mItemList;

    public ManageWaitingPersonAdapter(Context a_context, ArrayList<String> a_itemList) {
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
                    .setMessage(mItemList.get(pos) + " 님을 명단에서 제외하시겟습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            String name = mItemList.get(pos);
                            mItemList.remove(pos);

                            for (int i = 0; i < DataApplication.testWaitingQueueDBList.size(); i++) {
                                if (DataApplication.testWaitingQueueDBList.get(i).getQueueName().equals(ManageWaitingPersonActivity.qName)) {
                                    WaitingQueue temp = DataApplication.testWaitingQueueDBList.get(i);
                                    temp.removeWPerson(name);

                                    DataApplication.testWaitingQueueDBList.set(i, temp);
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
    public ManageWaitingPersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.manage_waiting_person_item, parent, false);
        return new ManageWaitingPersonViewHolder(view, mItemLongClickListener).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull ManageWaitingPersonViewHolder holder, int position) {
        final String waitingPerson = mItemList.get(position);

        holder.txtName.setText(waitingPerson);
        holder.txtCnt.setText(String.valueOf(position + 1) + ".");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}