package binderhook;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by randy on 2016/11/28.
 */
public class BinderHookHandler implements InvocationHandler {

    private static final String TAG = "BinderHookHandler";

    //原始的服务对象
    Object base;

    Class<?> stub;

    //constructor


    public BinderHookHandler(IBinder base) {

        try {
            //实际上asInterface方法是接口类IClipboard的内部抽象类Stub的方法，所以为了获取asInterface方法
            //这里首先获取该Stub类
            this.stub = Class.forName("android.content.IClipboard$Stub");
            //利用反射的方式获取到stubClass（IClipboard.Stub）的静态公有方法asInterface
            Method asInterfaceMethod = this.stub.getDeclaredMethod("asInterface", IBinder.class);
            //触发该方法的调用，asInterface为静态的，所以第一个参数可以置为null
            //第二个参数就是实际的服务对象，对应的就是内核中的IBinder实体
            //这两句的意思就是通过代理对象调用asInterface方法，根据传入的实际的服务对象（IBinder实体），
            // 最终返回一个经过改造的IBinder对象
            this.base = asInterfaceMethod.invoke(null, base);
        }catch (Exception e){
            throw new RuntimeException("hook is failed!");
        }

    }

    /**
     * 重写invoke方法，实现对剪贴板内部某些方法的篡改操作
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("getPrimaryClip".equals(method.getName())){

            Log.e(TAG, "Hook getPrimaryClip");
            return ClipData.newPlainText(null, "you are hooked!");
        }
        //测试，hasPrimaryClip用于判断剪贴板是否有内容，这里简单的让其一直为真
        if ("hasPrimaryClip".equals(method.getName())){
            return true;
        }
        return method.invoke(base, args);//触发方法调用
    }
}
