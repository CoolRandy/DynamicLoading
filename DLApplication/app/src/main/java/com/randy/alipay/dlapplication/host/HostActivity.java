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
                //这里sd卡中的plugin.apk文件时首先运行MainActivity生成的apk文件（默认在/data/app目录下），
                //手动移到下面的目录下的并重新命名
                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, "/mnt/sdcard/DynamicLoadHost/plugin.apk");
                startActivity(intent);
            }
        });
    }
}
