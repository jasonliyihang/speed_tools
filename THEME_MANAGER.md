# android 插件式无刷新换肤 兼容android 8.1

------

由于公司需要使用换肤功能，市面上一些框架代码都是比较低版本的，而且有些功能不能够满足，所以就自己写了一款基于apk插件式无刷新换肤框架-CXThemeManger ，代码版本使用 android api 28 开发。


## CXThemeManager 特点和功能

> * 支持apk插件式换肤
> * 支持属性替换
> * 更新主题不需要刷新
> * 支持扩展替换属性
> * 支持字体大小切换
> * 最低兼容android 2.3



## CXThemeManager 使用

### 1. 引用库：

```java

implementation 'com.liyihangjson:speed_tools:1.1.1'


```

### 2. 创建Application实现初始化

```java
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

```java
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


### 3. 创建要被替换的属性，只有符合属性替换的命名的并且支持替换属性才能被替换，其他属性不会受影响，默认替换属性有：Background，TextColor， Src 属性 ，如果还有属性可以扩展，

