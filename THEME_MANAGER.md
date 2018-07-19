# android 插件式无刷新换肤和字体大小切换 兼容android 8.1

------

由于公司需要使用换肤功能，市面上一些框架代码都是比较低版本的，而且有些功能不能够满足，所以就自己写了一款基于apk插件式无刷新换肤框架-CXThemeManger ，代码版本使用 android api 28 开发。


## CXThemeManager 特点和功能


1、支持apk插件式换肤

2、支持属性替换

3、更新主题不需要刷新

4、支持扩展替换属性

5、支持字体大小切换

6、最低兼容android 2.3



## CXThemeManager 使用

### 1. 引用库：


lib_speed_tools 核心库文件




### 2. 创建Application实现初始化

```
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // theme font size
        CXFontManager.getInstance().init(this);
        // theme init
        CXThemeManager.getInstance().init(this);

    }
}
```
CXThemeManager是主题管理器， CXFontManager是字体管理器。



### 3. 创建基类注册代码

```
public class BaseActivity extends AppCompatActivity implements CXUpdateUIListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CXThemeManager.getInstance().registerUpdateUI(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        CXThemeManager.getInstance().unRegisterUpdateUI(this);
        super.onDestroy();
    }

    @Override
    public void updateUI(boolean isFistLoading) {
        // call code update time
        Toast.makeText(this, "layout no refresh ok", Toast.LENGTH_SHORT).show();
    }
}
```


### 3. 默认支持替换属性有Background，TextColor， Src， 并且需要属性值名称符合命名规则，默认规则是属性值名称加上前缀 cxt_ ,支持字体大小切换的前缀是 cxf_ ;接下来创建被替换的属性值。

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#3F51B5</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#FF4081</color>


    <!--support theme color-->
    <color name="cxt_test">#ff0000</color>
</resources>

```

在mipmap中放一张被替换的图片 cxt_sever.jpg


### 4  接下来创建皮肤工程

black_theme  黑色 皮肤工程

必须包含上面的被替换的属性值。



### 5  开始使用

运行皮肤工程，将build apik 放入主工程的 assets 根目录中； 创建布局文件：


```

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/mainLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/cxt_test"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/holo_blue_bright"
        android:text="this is no refresh view update UI"
        android:textSize="@dimen/cxf_normal"
        android:textColor="@color/cxt_test" />

    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:src="@mipmap/cxt_sever" />

    <Button
        android:textSize="@dimen/cxf_normal"
        android:id="@+id/fontSize"
        android:onClick="onClick"
        android:text="change font size"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <Button
        android:textSize="@dimen/cxf_normal"
        android:id="@+id/restoreFont"
        android:onClick="onClick"
        android:text="restore font size"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <Button
        android:textSize="@dimen/cxf_normal"
        android:id="@+id/change"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:onClick="onClick"
        android:text="click change theme" />

    <Button
        android:textSize="@dimen/cxf_normal"
        android:onClick="onClick"
        android:id="@+id/restore"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="restore" />


</LinearLayout>

```



代码：


```

package com.liyihang.jason.theme_dome;

import android.os.Bundle;
import android.view.View;

import com.chengxing.cxsdk.CXFontManager;
import com.chengxing.cxsdk.CXThemeManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String apk_file_name="black_theme-debug.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.change)
        {
            CXThemeManager.getInstance().changeTheme(apk_file_name).sendUpdateUIAction();
        }
        if (view.getId()==R.id.restore)
        {
            CXThemeManager.getInstance().changeTheme(CXThemeManager.DEFAULT_THEMES).sendUpdateUIAction();
        }
        if (view.getId()==R.id.fontSize)
        {
            CXFontManager.getInstance().changeConfig(40).updateUI();
        }

        if (view.getId()==R.id.restoreFont)
        {
            CXFontManager.getInstance().changeConfig(0).updateUI();
        }

    }
}


```




