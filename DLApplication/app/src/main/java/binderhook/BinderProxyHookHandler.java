package binderhook;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by randy on 2016/11/28.
 * 伪造IBinder对象
 */
public class BinderProxyHookHandler implements InvocationHandler{

    private static final String TAG = "BinderProxyHookHandler";

    IBinder base;

//    Class<?> stub;

    Class<?> iinterface;

    public BinderProxyHookHandler(IBinder base) {
        this.base = base;
        try {
            //
//            this.stub = Class.forName("android.content.IClipboard$Stub");
            this.iinterface = Class.forName("android.content.IClipboard");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())){

            Log.e(TAG, "hook queryLocalInterface");

//            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
//                    new Class[]{IBinder.class, IInterface.class, this.iinterface},
//                    new BinderHookHandler(base, stub));
            //下面要具体hook剪贴板中的相关操作，即对相应方法进行处理，所以这里的第二个参数对应aidl编译
            //运行生成的接口类android.content.IClipboard，其中包括所有定义的方法
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                    new Class[]{this.iinterface},
                    new BinderHookHandler(base));
        }

        Log.e(TAG, "method: " + method.getName());
        //其他情况正常调用并返回
        return method.invoke(base, args);
    }
}
