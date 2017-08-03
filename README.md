# Speed Tools  V1.0

项目介绍：

speedtools 是一款基于代理模式的动态部署apk热更新框架、插件化开发框架；

speedtools这个名字主要指的快速迭代开发工具集的意思。

	

功能与特性：

1、支持Android 2.3 以上版本

2、支持R文件资源直接调用

3、开发过程中无发射调用

4、apk无需安装直接调用

5、代理模式对代码侵入性少

6、使用简单，只需要继承简单的类即可




项目结构：

module_client_one、module_client_two 是apk插件工程

module_host_main 是宿主工程 （基本不足任何事情只是加载apk工程）

lib_speed_tools 主要核心类工程 （自己应用只要依赖就行）




使用方法：

单独编译module_client_one、module_client_two，将apk加入sd卡。

然后修改路径即可运行。




