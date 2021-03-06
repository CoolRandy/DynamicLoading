package com.randy.alipay.dlapplication.client;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState){

        mProxyActivity.setContentView(generateContentView(mProxyActivity));
    }

    private View generateContentView(final Context context){

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setBackgroundColor(Color.parseColor("#F79AB5"));
        Button button = new Button(context);
        button.setText("launch");
        linearLayout.addView(button, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Launch Activity", Toast.LENGTH_SHORT).show();
                startActivityByProxy("com.randy.alipay.dlapplication.client.TestActivity");
            }
        });

        return linearLayout;
    }
}
