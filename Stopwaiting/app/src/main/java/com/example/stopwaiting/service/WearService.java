package com.example.stopwaiting.service;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class WearService extends WearableListenerService {
    private ArrayList<WearQueueDTO> qDTO = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/my_path")) {
            final String message = new String(messageEvent.getData());

            wearRequest();
            setWearOS();

            Log.e("test", message);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    public void setWearOS() {
        ((DataApplication) getApplication()).sendRefresh();
        ((DataApplication) getApplication()).sendUserInfo();

        ArrayList<WaitingInfo> tempList = new ArrayList<>();
        for (int i = 0; i < DataApplication.waitingList.size(); i++) {
            for (int j = 0; j < ((DataApplication) getApplication()).myWaiting.size(); j++) {
                WaitingInfo tempA = new WaitingInfo();
                tempA.setName(DataApplication.myWaiting.get(j).getQueueName());
                if (DataApplication.waitingList.get(i).getName().equals(DataApplication.myWaiting.get(j).getQueueName()) &&
                        !tempList.contains(tempA.getName())) {
                    tempList.add(DataApplication.waitingList.get(i));
                    break;
                }
            }
        }

        sendMyQueueInfo();
    }

    public void wearRequest() {
        DataApplication.myWaiting = new ArrayList<>();
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.myWaiting.size(); i++) {
                WaitingInfo selectInfo = new WaitingInfo();
                for (int j = 0; j < DataApplication.waitingList.size(); j++) {
                    if (DataApplication.waitingList.get(j).getName().equals(DataApplication.myWaiting.get(i).getQueueName())) {
                        selectInfo = DataApplication.waitingList.get(j);
                        break;
                    }
                }

                WearQueueDTO selectItem = new WearQueueDTO(DataApplication.currentUser, DataApplication.myWaiting.get(i), selectInfo);
                qDTO.add(selectItem);
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", DataApplication.currentUser.getStudentCode());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ((DataApplication) getApplication()).serverURL + "/wear", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    WearQueueDTO data = new WearQueueDTO();

                                    data.setQueueName(dataObject.getString("queueName"));
                                    data.setQId(dataObject.getLong("queueId"));
                                    data.setTime(dataObject.getString("time"));
                                    data.setMyNum(dataObject.getInt("myNum"));
                                    data.setLatitude(dataObject.getDouble("latitude"));
                                    data.setLongitude(dataObject.getDouble("longitude"));

                                    qDTO.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "신청한 웨이팅 조회 실패.", Toast.LENGTH_SHORT).show();
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

    public void sendMyQueueInfo() {
        byte[] serializedMember = null;

        for (int i = 0; i < qDTO.size(); i++) {
            WearQueueDTO selectItem = qDTO.get(i);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(selectItem);
                    serializedMember = baos.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Asset temp = Asset.createFromBytes(Base64.getEncoder().encode(serializedMember));
            PutDataMapRequest dataMap;
            if (i == 0) {
                dataMap = PutDataMapRequest.create(DataApplication.path + "/myWaiting_first");
            } else {
                dataMap = PutDataMapRequest.create(DataApplication.path + "/myWaiting");
            }
            dataMap.getDataMap().putAsset("myWaiting", temp);

            PutDataRequest request = dataMap.asPutDataRequest();
            Task<DataItem> putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);
        }
    }

}
