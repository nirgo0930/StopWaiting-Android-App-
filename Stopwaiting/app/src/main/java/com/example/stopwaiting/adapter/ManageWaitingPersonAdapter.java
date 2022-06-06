package com.example.stopwaiting.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.stopwaiting.R;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.ManageWaitingPersonActivity;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.viewholder.ManageWaitingPersonViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                    .setPositiveButton("신고하기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            UserInfo userInfo = mItemList.get(pos);

                            cancelUserRequest(ManageWaitingPersonActivity.qId, userInfo.getStudentCode(), pos);
                            reportUserRequest(userInfo.getStudentCode());
                        }
                    })
                    .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            UserInfo userInfo = mItemList.get(pos);

                            cancelUserRequest(ManageWaitingPersonActivity.qId, userInfo.getStudentCode(), pos);
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

    public void cancelUserRequest(Long qId, Long sId, int pos) {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testWaitingQueueDBList.size(); i++) {
                if (DataApplication.testWaitingQueueDBList.get(i).getQId().equals(qId)) {
                    WaitingQueue temp = DataApplication.testWaitingQueueDBList.get(i);

                    temp.removeWPerson(DataApplication.currentUser.getStudentCode());
                    DataApplication.testWaitingQueueDBList.set(i, temp);
                    break;
                }
            }
        } else {
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

    public void reportUserRequest(Long sId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, DataApplication.serverURL + "/user/" + sId + "/report", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(mContext, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
    }
}