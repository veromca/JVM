package com.lsq.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MyClassLoaderWithEncryption extends ClassLoader {
    public static final int seed = 0B10110110;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("E:\\mywork\\JVM\\out\\production\\JVM\\", name.replace(".", "/").concat(".lsqclass"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = fis.read()) != 0) {
                baos.write(b ^ seed);
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

    private static void encFile(String name) throws Exception {
        File f = new File("E:\\mywork\\JVM\\out\\production\\JVM\\", name.replace(".", "/").concat(".class"));
        FileInputStream fileInputStream = new FileInputStream(f);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\mywork\\JVM\\out\\production\\JVM\\", name.replace(".", "/").concat(".lsqclass")));
        int b = 0;
        while ((b = fileInputStream.read()) != 0) {
            fileOutputStream.write(b ^ seed);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws Exception {
        encFile("com.lsq.jvm.classloader.HelloClassLoader");
        ClassLoader cl = new MyClassLoaderWithEncryption();
        Class clazz = cl.loadClass("com.lsq.jvm.classloader.HelloClassLoader");
        HelloClassLoader l = (HelloClassLoader) clazz.newInstance();
        l.m();
        System.out.println(l);
        System.out.println(cl.getClass().getClassLoader());
        System.out.println(cl.getParent());
        System.out.println(cl.getParent().getClass().getClassLoader());
        System.out.println(getSystemClassLoader());

    }


}
