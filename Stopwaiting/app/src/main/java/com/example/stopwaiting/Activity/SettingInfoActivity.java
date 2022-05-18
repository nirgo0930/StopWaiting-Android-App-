package com.example.stopwaiting.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.DTO.ImgItem;
import com.example.stopwaiting.R;
import com.example.stopwaiting.Adapter.SettingImageAdapter;
import com.example.stopwaiting.DTO.WaitingInfo;
import com.example.stopwaiting.DTO.WaitingQueue;

import java.util.ArrayList;


public class SettingInfoActivity extends AppCompatActivity {
    private EditText edtName, edtDetail, edtInfo, edtPerson;
    private RecyclerView recyclerView;
    private RadioGroup rdoGroup;
    private RadioButton rdoNormal, rdoTime;
    private Button btnNext;
    private Intent settingInfoIntent;
    private SettingImageAdapter imgListAdapter;
    private ArrayList<Uri> uriList;
    public static Activity setting_info_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_info);
        settingInfoIntent = getIntent();
        setting_info_Activity = SettingInfoActivity.this;
        this.getContentResolver();
        edtName = findViewById(R.id.edtName);
        edtDetail = findViewById(R.id.edtLocDetail);
        edtInfo = findViewById(R.id.edtInfo);
        edtPerson = findViewById(R.id.edtMaxPerson);
        recyclerView = findViewById(R.id.lstImg);
        rdoGroup = findViewById(R.id.rdoGroup);
        rdoNormal = findViewById(R.id.rdoNormal);
        rdoTime = findViewById(R.id.rdoTime);
        btnNext = findViewById(R.id.btnSettingNext);

        uriList = new ArrayList<>();
        imgListAdapter = new SettingImageAdapter(this, uriList);
        recyclerView.setAdapter(imgListAdapter);

        imgListAdapter.notifyDataSetChanged();

        rdoNormal.setChecked(true);
        rdoNormal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setText("완료");
            }
        });
        rdoTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setText("다음");
            }
        });
        bindInsert();
        bindDelete();
        bindNext();
    }

    private void bindInsert() {
        findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {   // 이미지를 하나라도 선택한 경우
                if (data.getClipData() == null) {
                    Uri imageUri = data.getData();
//                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uriList.add(imageUri);
                } else {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        try {
                            uriList.add(imageUri);
                        } catch (Exception e) {
                            Log.e("Tag", "File select error", e);
                        }
                    }
                }
                imgListAdapter = new SettingImageAdapter(getApplicationContext(), uriList);
                recyclerView.setAdapter(imgListAdapter);
            }
        } else {

        }
    }

    private void bindDelete() {
        findViewById(R.id.btnRemove).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri recyclerItem = imgListAdapter.getSelected();
                if (recyclerItem == null) {
                    Toast.makeText(SettingInfoActivity.this, "항목을 선택 후 삭제해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                uriList.remove(recyclerItem);

                imgListAdapter.notifyDataSetChanged();
                final int checkedPosition = imgListAdapter.getCheckedPosition();
                imgListAdapter.notifyItemRemoved(checkedPosition);

                imgListAdapter.clearSelected();
            }
        });
    }

    private void bindNext() {
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoNormal) {
                    //서버 데이터 전송
                    ((DataApplication) getApplication()).testDBList.add(new WaitingInfo(((DataApplication) getApplication()).userId, 10L,
                            settingInfoIntent.getDoubleExtra("latitude", 0),
                            settingInfoIntent.getDoubleExtra("longitude", 0),
                            edtName.getText().toString(), edtDetail.getText().toString(), edtInfo.getText().toString(),
                            "normal", Integer.valueOf(edtPerson.getText().toString())));
                    //
                    if (uriList.size() != 0) {
                        String wname = edtName.getText().toString();
                        for (int i = 0; i < uriList.size(); i++) {
                            ((DataApplication) getApplication()).testImageDBList.add(new ImgItem(wname, (long) i + 1, uriList.get(i)));
                        }
                    }
                    ((DataApplication) getApplication()).testWaitingQueueDBList.add(
                            new WaitingQueue(edtName.getText().toString(), "normal", Integer.valueOf(edtPerson.getText().toString())));

                    Intent temp = new Intent(SettingInfoActivity.this, MyPageActivity.class);
                    temp.putExtra("userID", settingInfoIntent.getStringExtra("userID"));
                    MyPageActivity.myPageActivity.finish();
                    startActivity(temp);
                } else if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoTime) {
                    settingInfoIntent.putExtra("name", edtName.getText().toString());
                    settingInfoIntent.putExtra("detail", edtDetail.getText().toString());
                    settingInfoIntent.putExtra("info", edtInfo.getText().toString());
                    settingInfoIntent.putExtra("maxPerson", Integer.valueOf(edtPerson.getText().toString()));
                    if (uriList.size() != 0) {
                        settingInfoIntent.putParcelableArrayListExtra("image", uriList);
                    } else {
                        settingInfoIntent.putParcelableArrayListExtra("image", null);
                    }
                    settingInfoIntent.setClass(SettingInfoActivity.this, SettingTimeActivity.class);

                    startActivity(settingInfoIntent);
                }
            }
        });
    }
}
