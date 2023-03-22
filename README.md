# taxi
一个乘客打车和司机抢单的实现

# 1. demo演示

乘客端 https://shuchaowen.com/client/passenger.html

司机端 https://shuchaowen.com/client/taxi.html

如无法打开请稍后再试，可能在维护或访问的人太多

----
# 2.运行

1.运行jar包,-p参数可指定端口号，默认为8080端口

乘客端 http://localhost:8080/client/passenger.html

司机端 http://localhost:8080/client/taxi.html

本地运行在某些浏览器下因为权限问题无法获取定位信息(推荐使用Firefox)

2.推荐将高德的key换成自己的,否则可能因访问限制无法使用，需要更新的地方：

https://github.com/wcnnkh/taxi/blob/master/taxi-example/src/main/resources/client/passenger.html

https://github.com/wcnnkh/taxi/blob/master/taxi-example/src/main/resources/client/taxi.html

申请地址: https://lbs.amap.com/
