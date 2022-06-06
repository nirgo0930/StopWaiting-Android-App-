package com.example.stopwaiting.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.stopwaiting.R;
import com.example.stopwaiting.activity.CheckMyWaitingActivity;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.MyPageActivity;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.viewholder.MyWaitingListItemViewHolder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWaitingListAdapter extends RecyclerView.Adapter<MyWaitingListItemViewHolder> {
    private Context mContext;
    private List<WaitingListItem> mItemList;

    public MyWaitingListAdapter(Context a_context, List<WaitingListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    public interface OnItemClickEventListener {
        void onItemClick(int pos);
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(int pos);
    }

    private OnItemClickEventListener mItemClickListener = new OnItemClickEventListener() {
        @Override
        public void onItemClick(int pos) {

            Intent data = new Intent();
            data.putExtra("id", mItemList.get(pos).getWId());
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
                            Long qId = mItemList.get(pos).getQId();

                            cancelWaitingRequest(qId, pos);
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
                .load(waitingItem.getImgUrl())
                .into(holder.imgItem);

        holder.txtName.setText(waitingItem.getName());
        holder.txtLocDetail.setText(waitingItem.getLocDetail());
        holder.txtWaitCnt.setText("대기중인 인원 :  " + String.valueOf(waitingItem.getWaitingCnt()) + " 명");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void cancelWaitingRequest(Long qId, int pos) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                DataApplication.serverURL + "/queue/" + qId + "/" + DataApplication.currentUser.getStudentCode(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(mContext, "선택한 웨이팅을 취소했습니다.", Toast.LENGTH_SHORT).show();

                        mItemList.remove(pos);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "취소에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        request.setShouldCache(false);
        DataApplication.requestQueue.add(request);
        Log.e("temp", request.toString());
    }

}