package binderhook;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by randy on 2016/11/28.
 * 进行具体的hook操作
 */
public class BinderHookHelper {

    private static final String TAG = "BinderHookHelper";


    public static void hookClipboardService() throws Exception{

        final String CLIPBOARD_SERVICE = "clipboard";
        /** @hide */
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
        //获取ServiceManager管理中的原始系统服务对象Clipboard，
        //这个实际上获取的是服务对象在内核中的Binder实体的引用？？
        //实际上获取的是远程服务对象的一个接口
        IBinder binder = (IBinder)getService.invoke(null, CLIPBOARD_SERVICE);
        Log.e(TAG, "change binder");
        //通过newProxyInstance生成的代理类都会绑定一个调用处理器（InvocationHandler）
        //而在该代理对象上的所有方法调用都会统一转发给该调用处理器的invoke方法来处理
        //所以对于queryLocalInterface的修改可以直接在实现InvocationHandler接口类中重写的invoke方法中进行
        //invoke方法中的Method参数可以对应获取到所有的代理类方法，比如queryLocalInterface
        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[]{IBinder.class},
                new BinderProxyHookHandler(binder));
        Log.e(TAG, "add hookedBinder into sCache");
        Field cacheField = serviceManager.getDeclaredField("sCache");
        //sCache是私有对象，需访问授权
        cacheField.setAccessible(true);
        //
        Map<String, IBinder> cache = (Map)cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);
    }

}
