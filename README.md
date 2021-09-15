# taxi
一个乘客打车和司机抢单的实现

# 1. demo演示

乘客端 https://shuchaowen.com/client/passenger.html

司机端 https://shuchaowen.com/client/taxi.html

如无法打开请稍后再试，可能在维护或访问的人太多

----
# 2.运行

1.下载jar

http://basc.io/io/github/wcnnkh/taxi-example/0.0.1-SNAPSHOT/taxi-example-0.0.1-SNAPSHOT.jar

或

下载源码自行打包，需要先打包父项目

https://github.com/wcnnkh/framework

https://github.com/wcnnkh/start

2.运行jar包,-p参数可指定端口号，默认为8080端口

乘客端 http://localhost:8080/client/passenger.html

司机端 http://localhost:8080/client/taxi.html

本地运行在某些浏览器下因为权限问题无法获取定位信息(推荐使用google)

3.推荐将高德的key换成自己的,否则可能因访问限制无法使用，需要更新的地方：

https://github.com/wcnnkh/taxi/blob/master/taxi-example/src/main/resources/client/passenger.html

https://github.com/wcnnkh/taxi/blob/master/taxi-example/src/main/resources/client/taxi.html

申请地址: https://lbs.amap.com/
