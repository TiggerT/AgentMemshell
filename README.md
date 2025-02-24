## 0x01 简介
主要通过JavaAgent与Javassist技术动态修改Tomcat运行过程中的核心类方法来实现注入内存马。该方式实现的注入可以绕过java-memshell-scanner等常规内存马检测项目的扫描。

## 0x02 版本支持
目前在Tomcat8系与9系均进行过测试，可以成功注入内存马，其他版本如果存在问题可能是org.apache.catalina.core核心类库有修改，可以尝试替换成其他核心类做修改和注入。

