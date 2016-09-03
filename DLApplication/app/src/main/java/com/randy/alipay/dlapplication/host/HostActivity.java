package com.randy.alipay.dlapplication.host;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.randy.alipay.dlapplication.R;

/**
 * Created by randy on 2016/9/2.
 * 宿主程序
 */
public class HostActivity extends Activity{
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_layout);
        mButton = (Button)findViewById(R.id.load_apk);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostActivity.this, ProxyActivity.class);
                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, "/mnt/sdcard/DynamicLoadHost/plugin.apk");
                startActivity(intent);
            }
        });
    }
}
