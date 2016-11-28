package com.randy.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by randy on 2016/11/15.
 *
 */
public class HookActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("Testing");
        setContentView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("http://www.baidu.com"));
                //注意这里需要使用全局的context，不能使用this，两者使用的Instrumentation不一样
//                getApplicationContext().startActivity(intent);
                startActivity(intent);

            }
        });

    }
    //Application的执行顺序：构造方法-》attachBaseContext->onCreate
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        try {
//            HookHelper.attachContext();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
