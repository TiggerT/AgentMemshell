

0x01 版本支持：

目前在Tomcat8系上做过测试，9系测试失败，原因是org.apache.catalina.core相关类做了修改，后续可能会增加更多版本的支持。

0x02 简介：

主要通过JavaAgent与Javassist技术动态修改Tomcat运行过程中的核心类方法来实现注入内存马。