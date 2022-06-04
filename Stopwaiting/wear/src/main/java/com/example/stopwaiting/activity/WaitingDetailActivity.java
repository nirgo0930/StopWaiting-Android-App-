package com.example.stopwaiting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.ActivityDetailBinding;
import com.example.stopwaiting.service.SendMessage;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WaitingDetailActivity extends Activity {

    //private TextView mTextView;
    private double latitude;
    private double longitude;
    private String location;
    private String qId;

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //setContentView(R.layout.activity_detail);


        Intent intent = getIntent();

        String text = intent.getStringExtra("text");
        String[] textList = text.split("/");

        location = textList[2];
        latitude = Double.parseDouble(textList[3]);
        longitude = Double.parseDouble(textList[4]);
        qId = textList[5];

        //TextView textLoc = findViewById(R.id.textLoc);
        //TextView textTime = findViewById(R.id.textTime);

        if(textList[0].equals("normal")){
            if(textList[1].equals("0")){
                binding.textTime.setText(DataApplication.currentUserInfo.getName()+"님이 체크인하실 차례에용");
            }
            else{
                binding.textTime.setText(textList[1]+"명 남았어용");
            }
        }
        else{
            binding.textTime.setText(textList[1]);
        }
        binding.textLoc.setText(location);

        //취소하기
        //Button btnCancel = (Button) findViewById(R.id.btnCancel);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(WaitingDetailActivity.this)
                        .setTitle(location)
                        .setMessage("해당 웨이팅을 취소하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //예약 취소로 바꾸기
                                String datapath = "/my_path";
                                String onClickMessage = String.valueOf(DataApplication.currentUserInfo.getStudentCode())
                                        + "/"+ qId;
                                new SendMessage(datapath,onClickMessage,WaitingDetailActivity.this).start();
                                Log.e("test", onClickMessage);
                            }
                        })
                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        //QR인식
        //Button btnQR = (Button) findViewById(R.id.btnQR);
        binding.btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                startActivity(intent);
            }
        });

        //위치확인
        //Button btnLoc = (Button) findViewById(R.id.btnLoc);
        binding.btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("location",location);
                startActivity(intent);
            }
        });

    }

//    class SendMessage extends Thread{
//        String path;
//        String message;
//
//        SendMessage(String p, String m){
//            path = p;
//            message = m;
//        }
//
//        public void run() {
//
//            //Get all the nodes//
//
//            Task<List<Node>> nodeListTask =
//                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
//            try {
//
//            //Block on a task and get the result synchronously//
//
//                List<Node> nodes = Tasks.await(nodeListTask);
//
//            //Send the message to each device//
//
//                for (Node node : nodes) {
//                    Task<Integer> sendMessageTask =
//                            Wearable.getMessageClient(WaitingDetailActivity.this).sendMessage(node.getId(), path, message.getBytes());
//                    try {
//                        Integer result = Tasks.await(sendMessageTask);
//                        //Handle the errors//
//                    } catch (ExecutionException exception) {
//                        //TO DO//
//                    } catch (InterruptedException exception) {
//                        //TO DO//
//                    }
//
//                }
//
//            } catch (ExecutionException exception) {
//
//                //TO DO//
//
//            } catch (InterruptedException exception) {
//
//                //TO DO//
//
//            }
//        }
//    }
}