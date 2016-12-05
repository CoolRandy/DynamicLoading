package binderhook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by randy on 2016/11/28.
 * 采用Binder Hook的方法无缝使用系统服务，这些系统服务内部是采用Binder机制提供给应用程序的
 * hook的关键首先要搞明白Binder原理以及系统服务的调用原理
 *
 */
public class BinderHookActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            BinderHookHelper.hookClipboardService();
        }catch (Exception e){
            e.printStackTrace();
        }

        EditText editText = new EditText(this);
        setContentView(editText);

    }
}
