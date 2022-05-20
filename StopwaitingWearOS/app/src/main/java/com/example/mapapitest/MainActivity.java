package com.example.mapapitest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    private TextView mTextView;
//    private ActivityMainBinding binding;

    private ListView listView;

    private ArrayAdapter<String> textWaitingAdapter;
    private ArrayList<String> waitingList;
    private Intent timeIntent;

    //임시 타입
    private String str;

    private String str2;
    private String str3;
    private String[] strList;
    private ArrayList<String> screenList;

    private String type;

    private String time;
    private String count;
    private String location;

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    public static final String NOTIFICATION_CHANNEL_ID = "4665";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //블루투스 어댑터를 디폴트 어댑터로 설정
        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때

            // 여기에 처리 할 코드를 작성하세요.

        }
        else { // 디바이스가 블루투스를 지원 할 때

            if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)

                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출

            } else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)

                // 블루투스를 활성화 하기 위한 다이얼로그 출력

                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                // 선택한 값이 onActivityResult 함수에서 콜백된다.

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(intent, REQUEST_ENABLE_BT);

            }

        }


        listView = findViewById(R.id.listView);

        waitingList = new ArrayList<>();

        screenList = new ArrayList<>();

        //이부분 서버에서 받아오는 리스트 부분
        str = "time/11:00/미용실";
        str2 = "time/12:00/북카페";
        str3 = "time/15:00/특식 배부";
        String str4 = "normal/5/zz";
        List<String> strs = new ArrayList<>();
        strs.add(str);
        strs.add(str2);
        strs.add(str3);
        strs.add(str4);


//        strList = str.split("/");
//
//        time=strList[1];
//        location = strList[2];
//
//        String text=time+" "+location;
//
//        //str =  "time/11:00/미용실";
//        //str2= "time/12:00/북카페";
//        waitingList.add(str);
//        waitingList.add(str2);`

        for (int i = 0; i < 4; i++) { //나중에 서버에서 받아온 수만큼 for문 돌리기
            strList = strs.get(i).split("/");
            if (strList[0].equals("normal")) {
                screenList.add("\t" + strList[1] + "명\t\t\t" + strList[2]);
            } else {
                screenList.add("\t" + strList[1] + "\t\t" + strList[2]);
            }
        }

        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);
        //textWaitingAdapter = new ListViewAdapter(this,R.layout.listview_item, screenList);

        listView.setAdapter(textWaitingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //waitingList.get(i);

                type = strList[0];
                Intent intent = new Intent(getApplicationContext(), WaitingDetailActivity.class);
                if (type.equals("time")) {
                    // 13:00 북카페
//                    time = strList[1];
//                    location = strList[2];
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                } else if (type.equals("normal")) {
                    // 5 북카페
//                    count = strList[1];
//                    location = strList[2];
//                    intent.putExtra("count",count);
//                    intent.putExtra("location",location);
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                }

            }
        });

//        Notification.Builder mBuilder =
//                new Notification.Builder(MainActivity.this)
//                .setSmallIcon(R.drawable.moomin)
//                .setContentTitle("제목")
//                .setContentText("내용");


        //////////
        int NOTIFICATION_ID = 234;

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap mLargeIcon =
                        BitmapFactory.decodeResource(getResources(), R.drawable.moomin22);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                        new Intent(getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
////
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(MainActivity.this)
//                                .setSmallIcon(R.drawable.moomin)
//                                .setContentTitle("제목")
//                                .setContentText("내용")
//                                .setDefaults(Notification.DEFAULT_SOUND)
//                                .setLargeIcon(mLargeIcon)
//                                .setPriority(Notification.PRIORITY_DEFAULT)
//                                .setAutoCancel(true)
//                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "my_channel_01";
                CharSequence name = "my_channel";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.enableLights(true);
                notificationManager.createNotificationChannel(mChannel);
//
                NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, id).setContentTitle("Title");


                notificationManager.notify(4665, notification.build());

            }
        });
    }

    public void selectBluetoothDevice() {
        //이미 페어링 되어있는 블루투스 기기를 찾기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        devices = bluetoothAdapter.getBondedDevices();

        //페어링 된 디바이스의 크기를 저장
        int pariedDeviceCount = devices.size();

        if (pariedDeviceCount == 0) {
            //페어링을 하기위한 함수 호출
        } else {//페어링 되어있는 장치가 있는 경우
            //디바이스를 선택하기 위한 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
            //페어링 된 각각의 디바이스의 이름과 주소를 저장
            List<String> list = new ArrayList<>();
            //모든 디바이스의 이름을 리스트에 추가
            for (BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }
            list.add("취소");

            //List를 CharSequence 배열로 변경
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);

            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너

            builder.setItems(charSequences, new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    // 해당 디바이스와 연결하는 함수 호출

                    connectDevice(charSequences[which].toString());

                }

            });


            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정

            builder.setCancelable(false);

            // 다이얼로그 생성

            AlertDialog alertDialog = builder.create();

            alertDialog.show();


        }

    }

    public void connectDevice(String deviceName) {

        // 페어링 된 디바이스들을 모두 탐색

        for (BluetoothDevice tempDevice : devices) {

            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (deviceName.equals(tempDevice.getName())) {

                bluetoothDevice = tempDevice;

                break;

            }

        }

        // UUID 생성

        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성

        try {

            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);

            bluetoothSocket.connect();

            // 데이터 송,수신 스트림을 얻어옵니다.

            outputStream = bluetoothSocket.getOutputStream();

            inputStream = bluetoothSocket.getInputStream();

            // 데이터 수신 함수 호출

            receiveData();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    public void receiveData() {

        final Handler handler = new Handler();

        // 데이터를 수신하기 위한 버퍼를 생성

        readBufferPosition = 0;

        readBuffer = new byte[1024];



        // 데이터를 수신하기 위한 쓰레드 생성

        workerThread = new Thread(new Runnable() {

            @Override

            public void run() {

                while(Thread.currentThread().isInterrupted()) {

                    try {

                        // 데이터를 수신했는지 확인합니다.

                        int byteAvailable = inputStream.available();

                        // 데이터가 수신 된 경우

                        if(byteAvailable > 0) {

                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.

                            byte[] bytes = new byte[byteAvailable];

                            inputStream.read(bytes);

                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.

                            for(int i = 0; i < byteAvailable; i++) {

                                byte tempByte = bytes[i];

                                // 개행문자를 기준으로 받음(한줄)

                                if(tempByte == '\n') {

                                    // readBuffer 배열을 encodedBytes로 복사

                                    byte[] encodedBytes = new byte[readBufferPosition];

                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    // 인코딩 된 바이트 배열을 문자열로 변환

                                    final String text = new String(encodedBytes, "US-ASCII");

                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {

                                        @Override

                                        public void run() {

                                            // 텍스트 뷰에 출력

                                            //textViewReceive.append(text + "\n");

                                        }

                                    });

                                } // 개행 문자가 아닐 경우

                                else {

                                    readBuffer[readBufferPosition++] = tempByte;

                                }

                            }

                        }

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                    try {

                        // 1초마다 받아옴

                        Thread.sleep(1000);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                }

            }

        });

        workerThread.start();

    }



}