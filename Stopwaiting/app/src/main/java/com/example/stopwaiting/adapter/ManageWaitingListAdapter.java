package com.example.stopwaiting.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.stopwaiting.R;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.ManageWaitingActivity;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.viewholder.ManageWaitingListItemViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageWaitingListAdapter extends RecyclerView.Adapter<ManageWaitingListItemViewHolder> {
    private Context mContext;
    private List<WaitingListItem> mItemList;

    public ManageWaitingListAdapter(Context a_context, List<WaitingListItem> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;
    }

    public interface OnItemClickEventListener {
        void onItemClick(int pos);
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(int pos);
    }

    private ManageWaitingListAdapter.OnItemClickEventListener mItemClickListener = new ManageWaitingListAdapter.OnItemClickEventListener() {
        @Override
        public void onItemClick(int pos) {
            Intent data = new Intent(mContext, ManageWaitingActivity.class);
            data.putExtra("qId", mItemList.get(pos).getQId());
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

                            removeWaitingRequest(mItemList.get(pos).getName());

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

        if (DataApplication.isTest) {
            Glide.with(mContext.getApplicationContext())
                    .load(waitingItem.getImgUri())
                    .into(holder.imgItem);
        } else {
            Glide.with(mContext.getApplicationContext())
                    .load(waitingItem.getImgUrl())
                    .into(holder.imgItem);
        }

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

    public void removeWaitingRequest(String name) {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                if (DataApplication.testDBList.get(i).getName().equals(name)) {

                    DataApplication.testDBList.remove(i);
                    break;
                }
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("waitingName", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/removeWaiting", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
//                            Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    try {
                        if (requestBody != null && requestBody.length() > 0 && !requestBody.equals("")) {
                            return requestBody.getBytes("utf-8");
                        } else {
                            return null;
                        }
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }
}