package com.pri.recordview.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.pri.recordview.R;
import com.pri.recordview.feature.PriRecordSet;
import com.pri.recordview.util.LogUtils;
import com.pri.recordview.util.RecordViewUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Switch mSwitchPhoneRecord;
    private Button mButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate ");
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        mSwitchPhoneRecord = (Switch) findViewById(R.id.switch_open_sidebar);
        mButton = (Button) findViewById(R.id.btn_record_manager);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.android.soundrecorder", "com.android.soundrecorder.RecordingFileList");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    LogUtils.e(TAG, "startActivity error: " + e.getMessage());
                }
            }
        });

        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume ");
        updateSwitchBar();
        mSwitchPhoneRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(TAG, "onCheckedChanged isChecked: " + isChecked);

                if (isChecked) {
                    PriRecordSet.putCallRecordValue(MainActivity.this, 1);
                    RecordViewUtil.goToFloatRecordService(MainActivity.this, true);
                } else {
                    PriRecordSet.putCallRecordValue(MainActivity.this, 0);
                    // stopService
                    RecordViewUtil.goToFloatRecordService(MainActivity.this, false);
                }
            }
        });
    }

    @Override
    public void onPause() {
        mSwitchPhoneRecord.setOnCheckedChangeListener(null);
        LogUtils.d(TAG, "onPause ");
        super.onPause();
    }

    private void updateSwitchBar() {
        boolean isSwitchOpen = PriRecordSet.isCallRecordEnable(this);
        LogUtils.d(TAG, "updateSwitchBar isSwitchOpen: " + isSwitchOpen);
        mSwitchPhoneRecord.setChecked(isSwitchOpen);
    }


}
