package com.randy.alipay.dlapplication.client;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by randy on 2016/9/3.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(mProxyActivity);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        button.setBackgroundColor(Color.YELLOW);
        button.setText("测试界面");
        setContentView(button);
    }
}
