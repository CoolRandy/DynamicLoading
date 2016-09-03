# DynamicLoading
动态加载
----------------------------
理论层面: 可以通过一个宿主程序来运行一些未安装的apk
实践层面：未安装的apk是不能被直接调起来的，可以采用宿主程序去动态加载apk文件并放到自己的进程中运行
存在的问题：1、资源的访问：因为将apk加载到宿主程序中去执行，就无法通过宿主程序的Context去取到apk中的资源,比如图片、文本等，这是很好理解的，因为apk已经不存在上下文了，它执行时所采用的上下文是宿主程序的上下文，用别人的Context是无法得到自己的资源的。
针对这个问题可以这样解决：将apk中的资源解压到某个目录，然后通过文件去操作资源---》该方法只是理论上可行

2、activity的生命周期问题：因为apk被宿主程序加载执行后，它的activity其实就是一个普通的类，正常情况下，activity的生命周期是由系统来管理的，现在被宿主程序接管了以后，如何替代系统对apk中的activity的生命周期进行管理是有难度的。
针对该问题，解决起来会简单些：可以在宿主程序中模拟activity的生命周期并合适地调用apk中activity的生命周期方法

工作原理：首先宿主程序到文件系统比如sd卡中去加载apk文件，然后通过代理activity去执行apk中的activity
关于动态加载apk，理论上可以用到的有DexClassLoader、PathClassLoader和URLClassLoader。
DexClassLoader ：可以加载文件系统上的jar、dex、apk
PathClassLoader ：可以加载/data/app目录下的apk，这也意味着，它只能加载已经安装的apk
URLClassLoader ：可以加载java中的jar，但是由于dalvik不能直接识别jar，所以此方法在android中无法使用，尽管还有这个类

关于jar、dex和apk，dex和apk是可以直接加载的，因为它们都是或者内部有dex文件，而原始的jar是不行的，必须转换成dalvik所能识别的字节码文件，转换工具可以使用android sdk中platform-tools目录下的dx
转换命令 ：dx --dex --output=dest.jar src.jar

----------------------------
注意点：1、首先以MainActivity作为Launcher，编译运行生成apk文件，默认情况下生成的apk文件位于/data/app/目录下，为了测试demo从sd卡中加载apk文件，可以采用adb的cp命令将该目录下的apk文件
复制到/mnt/sdcard/DynaminLoadHost/目录下，并使用rename命令重命名为plugin.apk。这时有一点需要注意，宿主程序加载apk中的MainActivity在该工程中是无法获取xml中定义的资源文件的，也就是说
要通过代码的方式定义布局，测试过程中出现一个问题：在设置LinearLayout的背景颜色时调用了getResource方法，是会报错的，因为该方法是查询资源ID，调用不到。如果后面改动了插件的代码，需要重新编译生成apk文件，这点也要注意。
2、然后以宿主Activity：HostActivity作为Launcher，编译运行点击button即可调起apk.有一个问题需要注意：从MainActivity点击跳转到TestActivity时，则出现了错误。后来发现是startActivityByProxy 方法的第二分支有问题，首先，PROXY_VIEW_ACTION 是一个自定义的 Action 字符串，所以需要在 APK 的配置文件中添加 intent-filter，但是，宿主程序并不能访问未安装的 APK 的配置文件，可以直接在代理activity即ProxyActivity的配置文件中添加intent-filter:
```java
<intent-filter>
    <action android:name="com.randy.alipay.dlapplication.host.VIEW"/>
    <category android:name="android.intent.category.DEFAULT"/>
</intent-filter>
```java
------------------------

上面的工程实现了动态加载加载sd卡上的apk文件，但是没有解决资源访问和Activity生命周期管理。调起未安装的apk从技术的角度不可能所有的情况都适用，调起的apk必须遵循某种约束。
插件化的目的是减小宿主程序apk包的大小，同时降低宿主程序的更新频率并做到自由装载模块。
在明确插件化目的的基础上思考解决方案：1、采用将apk中的资源文件赋值一份到宿主程序中，显然就不可行了，这样首先会增加宿主程序包的大小，其次每次发布插件都需要将资源拷贝到宿主程序，也就是说需要每次更新宿主程序包，所以从这两点看都是违背插件化思想的。2、思路二就是将apk中的资源解压出来，然后以文件流的形式去读取资源，理论可行，实际操作则比较复杂，因为首先文件类型太多，不同资源有不同的文件流格式，其次针对不同设备加载方式也是不一样的，还有就是如何去选择合适的资源，这些问题都需要考虑并解决。

任教主在这里采用的是下面的实现方式：

分析Context的源码可以知道Activity的主要工作是由ContextImpl来完成的。注意到Context中有两个抽象方法：
