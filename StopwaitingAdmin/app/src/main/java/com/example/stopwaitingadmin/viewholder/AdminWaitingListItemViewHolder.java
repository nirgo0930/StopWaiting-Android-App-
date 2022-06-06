package com.example.stopwaitingadmin.viewholder;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stopwaitingadmin.R;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.activity.DataApplication;
import com.example.stopwaitingadmin.activity.WaitingListActivity;
import com.example.stopwaitingadmin.adapter.AdminWaitingListAdapter;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminWaitingListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtUser, txtLocation;
    public Button btnAccept, btnReject;
    public AdminWaitingListAdapter adapter;
    private RecyclerView mWaitingListView;
//    private List<AdminWaitingListItem> mItemList;

    public AdminWaitingListItemViewHolder(View a_view) {
        super(a_view);
        imgItem = a_view.findViewById(R.id.imgItem);
        txtName = a_view.findViewById(R.id.txtId);
        txtUser = a_view.findViewById(R.id.txtUser);
        txtLocation = a_view.findViewById(R.id.txtNum);
        btnAccept = a_view.findViewById(R.id.btnTrue);
        btnAccept.setOnClickListener(view -> {

            //승인된 항목 개설된 가게 DB로 이동 필요( 승인 대기중 DB에서 삭제 및 개설된 가게 DB에 등록)
            Log.e("pos------------------------------------------------", String.valueOf(getAdapterPosition()));
            AcceptResultRequest(adapter.mItemList.get(getAdapterPosition()));

            adapter.mItemList.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
        });
        btnReject = a_view.findViewById(R.id.btnFalse);
        btnReject.setOnClickListener(view -> {

            //거부된 항목 승인 대기중 DB에서 삭제 필요
            RejectResultRequest(adapter.mItemList.get(getAdapterPosition()));

            adapter.mItemList.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
        });
    }

    public AdminWaitingListItemViewHolder linkAdapter(AdminWaitingListAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public void AcceptResultRequest(AdminWaitingListItem data) {
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("status", "CONFIRMED");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, DataApplication.serverURL + "/waitinginfo/" + data.getId() + "/status", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        adapter.mItemList.remove(getAdapterPosition());
//                        adapter.notifyItemRemoved(getAdapterPosition());
//                        WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    public void RejectResultRequest(AdminWaitingListItem data) {
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("status", "CANCELED");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, DataApplication.serverURL + "/waitinginfo/" + data.getId() + "/status", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        adapter.mItemList.remove(getAdapterPosition());
//                        adapter.notifyItemRemoved(getAdapterPosition());
//                        WaitingListActivity.txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(adapter.mItemList.size()) + "건 존재합니다.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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