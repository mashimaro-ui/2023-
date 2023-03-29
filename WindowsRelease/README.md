# Huawei2023

#### 介绍
32强就算成功！

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)



### 提交判题器

 Linux

./Robot_gui -m ./maps/1.txt -c ./SDK/src "java com.huawei.codecraft.Main"  

Windows11

 ./Robot_gui.exe -m ./maps/1.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   

 ./Robot_gui.exe -f -m ./maps/1.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   

Windows10

.\Robot_gui.exe -m .\maps\1.txt -c .\SDK\java\src "java com.huawei.codecraft.Main"

.\Robot_gui.exe  -f -m .\maps\1.txt -c .\SDK\java\src "java com.huawei.codecraft.Main"

```
// 错误: 编码GBK的不可映射字符
// javac 默认按照本机语言（GBK） 编译代码 
// 而我们的代码是 utf-8 编码，所以会报错
javac -encoding UTF-8  xxx.java
javac *.java -encoding utf-8
```

