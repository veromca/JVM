package com.lsq.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        File f = new File("c:/test/", name.replace(".", "/").concat(".class"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b=fis.read()) !=0) {
                baos.write(b);
            }
            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();//可以写的更加严谨
            return defineClass(name, bytes, 0, bytes.length);// 将二进制流转换为Class对象
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name); //throws ClassNotFoundException
    }

    public static void main(String[] args) throws Exception{
        ClassLoader cl = new MyClassLoader();
        Class clazz = cl.loadClass("com.lsq.jvm.classloader.HelloClassLoader");
        Class clazz1 = cl.loadClass("com.lsq.jvm.classloader.HelloClassLoader");
        System.out.println(clazz == clazz1);
        HelloClassLoader helloClassLoader = (HelloClassLoader)clazz.newInstance();
        helloClassLoader.m();
        System.out.println(cl.getClass().getClassLoader());
        System.out.println(cl.getParent());
        System.out.println(cl.getParent().getClass().getClassLoader());
        System.out.println(getSystemClassLoader());

    }


}
