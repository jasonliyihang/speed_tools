# Speed-Tools  V1.1.1

项目介绍：

speed-tools 是一款基于代理模式的动态部署apk热更新框架、插件化开发框架；

android api 升级至 28 版本

apk plugin framework，

apk plugin theme manager

apk plugin font size manager



重要更新：

加入apk插件化换肤功能

加入字体大小控制功能

支持属性换肤

支持无刷新换肤

	

功能与特性：

1、支持Android 2.3 以上版本

2、支持R文件资源直接调用

3、开发过程中无发射调用

4、apk无需安装直接调用

5、代理模式对代码侵入性少

6、使用简单，只需要继承简单的类即可



项目结构：

lib_speed_tools: 插件化核心功能library

module_host_main：宿主工程主工程，负责加载部署apk

module_client_one:测试业务apk 1

module_client_two:测试业务apk 2

lib_img_utils:测试imageloader图片框架(用于测试第三方库是否正常执行)


换肤功能和字体大小功能：

theme_dome 换肤功能dome

black_theme 换肤皮肤包



speed-tools插件化框架使用：

引用：
```
implementation 'com.liyihangjson:speed_tools:1.1.1'

```




如果对你有帮助，不要忘了start一下哦！！！ ^.^