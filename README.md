# wps-view-java

## 本次更新01-23
新增对minio对象存储支持
感谢[zhuoran提交的PR](https://gitee.com/zhuoran007)

## 本次更新11-24
1. 集成wps官方文档转换API,配合VUE前端项目,实现文件的转换
2. 代码样式调整
3. 用阿里oss的用户，增加一个阿里官方转换文档方法

## 本次更新06-28
1. 升级oss-sdk（强，不升级可能导致上传oss报错）
2. 修改在线文档预览获取文件大小

## 本次更新05-07
1. 增加七牛文件存储，可在配置文件中配置切换
2. 重构部分结构

## 本次更新03-10
1. 增加前端调用的新建模版接口，快速新建文件
2. w_user_acl_t权限表中rename字段更改为re_name，避免和mysql关键字冲突报错
3. 满足你的OSS所有需求，util中OSSUtil包含了所有常用的oss方法，全部测试可用
4. 增加上传、删除等文件管理接口

### 演示地址
vue:
[https://ljserver.cn/wpsonline](https://ljserver.cn/wpsonline)

react:
[https://ljserver.cn/wpsonline-react](https://ljserver.cn/wpsonline-react)

## 环境准备
1. 前提
    - 申请[wps在线编辑服务](https://open.wps.cn/weboffice/)，获取到appid和appsecret
    - 设置wps回调url，如http://123.34.56.78:8080（此端口号必须与java项目中端口号一致）
    - 申请阿里oss，获取到access_key和access_secret等相关参数（其它云存储请自行集成）
        - 不想使用oss的，或者是想使用其它云，以及服务文件的，可屏蔽版本更新接口中的代码，或者重构，或者不管（只是保存版本的时候有异常），先整体拉通工程
    - java编译器安装lombok插件，详见[lombok安装说明](https://blog.csdn.net/qq_23501739/article/details/91559450)
2. 服务
    - 初始化mysql，执行resources下的wpsinit.sql文件（数据库可自己改，以及其中文件url，头像url，请自行填写）
    - 配置resources下application.properties中的mysql、wps以及oss相关参数
    - 打包后部署到回调服务器（注意端口号）
    - 其它相关接口请查阅[wps开放平台文档](http://open-doc.wps.cn/)
3. 设计
    - 详见resources下的 导图.png 和 表结构.png
4. 前端代码地址

    vue:
[https://gitee.com/mose-x/wps-view-vue](https://gitee.com/mose-x/wps-view-vue)

    react:
[https://gitee.com/mose-x/wps-view-react](https://gitee.com/mose-x/wps-view-react)

## 配置文件中的参数部分可做参考
## 代码结构可自行调整

## 彩蛋
ApplicationTests类文件中，有个OSS地址转在线预览地址方法，以及文档转换方法，但是是收费的，相关费用自行了解
<br>

#### 感谢
[caizhangwei提供的反馈](https://gitee.com/cai_zhang_wei)

[zhuoran提交的minio对象存储PR](https://gitee.com/zhuoran007)
<br/>

## 特别鸣谢
#### [JetBrains](https://www.jetbrains.com/?from=wps-view-java) 提供的license
<img src="https://ossfile.ljserver.cn/uPic/jetbrains-variant-3.svg" alt="jetbrains-variant-3" style="zoom:0.1%;" />
