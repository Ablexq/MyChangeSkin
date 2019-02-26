

# 参考：

[Android换肤方案总结](https://www.jianshu.com/p/b0253de8ac04)

[Android主题切换（Theme）实现日夜间功能](https://www.jianshu.com/p/0cd03c878def?open_source=weibo_search)


动态加载demo： https://github.com/Ablexq/SkinDemo



# 简介：

换肤其实就是替换资源（文字、颜色、图片等）

Android换肤在使用场景上可以区分为【静态换肤/动态换肤】、【应用内换肤/插件式换肤】。

不同的换肤方案，适用于不同的业务场景。

setTheme可以较好的支持静态换肤，但如果要动态换肤则需要解决的核心问题有：

外部资源的加载

定位到需要换肤的View

第一个资源加载的问题可以通过构造AssetManager，反射调用其addAssetPath就可以完成。

第二个问题，就可以利用在onCreateView中，根据view的属性来定位。
应用内换肤一般以资源前缀或后缀来区分不同资源，辅助以tag或侵入式等来区分换肤的view；
进而保存需要换肤的view引用。



### 主题换肤（Theme）和插件换肤（APK换肤）。

###### 插件换肤

插件换肤的实现原理就是主APK根据当前环境需求，解析指定目录下对应的插件APK，获得其中同名的资源文件并动态替换到主APK的应用程序中。插件APK并不需要安装，只需要放置在指定目录下即可。

动态换肤可以满足日常产品和运营需求，满足用户个性化界面定制的需求等等。

动态换肤，相比于静态皮肤，可以减小apk大小

皮肤模块独立便于维护

由服务器下发，不需要发版即可实现动态更新

优点： 能够实现各种主题样式的加载，比较灵活，需要增添新的主题只要新建一个插件APK，并配置好相关的资源，放置到指定的文件目录下就行，很方便。

缺点： 需要对控件进行适配修改，实现换肤功能，对于自定义控件，也需要在适配上花点时间。而且放置在文件夹中的插件APK也可能会因为被误删或是损坏而造成资源获取不到，导致换肤失败。



###### 主题换肤

主题换肤的实现原理就是在主apk配置多套主题，每套主题对同一个属性使用相应的资源。

内置换肤,在Apk包中存在多种资源(图片、颜色值)用于换肤时候切换。

自由度低，apk文件大  一般用于没有其他需求的日间/夜间模式app 。

优点： 相比插件换肤来说更容易上手，理解起来也会更容易。

缺点： 增添新的主题样式必须要发布新版本。
全部资源文件都放在APK中，APK会显得十分臃肿，特别是图片资源，因此个人推荐纯色线条的图标，并通过着色来实现不同主题下换肤的可能。


# 应用内、静态换肤示例：

res/values/attrs:

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="button_bg" format="reference|color" />
    <attr name="activity_bg" format="reference|color" />
    <attr name="text_cl" format="reference|color" />
</resources>
```

res/values/styles:

```
    <style name="AppBaseTheme" parent="android:Theme.Light">
    </style>


    <style name="AppTheme" parent="AppBaseTheme">
        <item name="text_cl">#ffffff</item>
        <item name="button_bg">#000000</item>
        <item name="activity_bg">#ffffff</item>
    </style>

    <style name="DarkTheme" parent="AppBaseTheme">
        <item name="text_cl">#000000</item>
        <item name="button_bg">#ffffff</item>
        <item name="activity_bg">#000000</item>
    </style>

```

布局：

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?activity_bg"
    android:gravity="center"
    android:orientation="vertical">

    <Button
        android:id="@+id/button0"
        android:layout_width="300dp"
        android:layout_height="100dp"
        android:background="?button_bg"
        android:text="@string/switch_theme"
        android:textColor="?text_cl" />

</LinearLayout>
```

定义全局变量:

```
public class MyApp extends Application {
    public static int theme = 0;

    public static MyApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static MyApp getApplication() {
        return application;
    }

}
```

设置默认主题：

```
    <application
        android:name=".MyApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```

activity中切换：

```

public class MainActivity extends Activity {

    public Button button0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.theme == 0) {
            //使用默认主题
            setTheme(R.style.AppTheme);
        } else {
            //使用自定义的主题
            setTheme(R.style.DarkTheme);
        }
        setContentView(R.layout.activity_main);

        button0 = (Button) this.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.theme != 0) {
                    MyApp.theme = R.style.AppTheme;
                    MyApp.theme = 0;
                } else {
                    MyApp.theme = R.style.DarkTheme;
                    MyApp.theme = 10;
                }
                recreate();
            }
        });
    }
}

```

缺点：

在此activityoncreate()中的setContentView()方法调用之前，判断当前的theme，并调用setTheme()，实现改变theme的效果。

注意：这种方法实现切换theme不是很友好，因为在activity重新创建时，可能会有闪屏的现象。























