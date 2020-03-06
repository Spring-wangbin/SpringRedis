package com.ssm.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class PropertyTest {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try{
            String url = PropertyTest.class.getClassLoader().getResource("").getPath();
            //读取属性文件jdbc.properties
            InputStream in = new BufferedInputStream (new FileInputStream(url+"/jdbc.properties"));
            prop.load(in);     ///加载属性列表
            prop.setProperty("jdbc.username","sa123");
            in.close();

            ///保存属性到jdbc.properties文件
            FileOutputStream oFile = new FileOutputStream(url+"/jdbc.properties", false);//true表示追加打开
            prop.store(oFile, "After modify properties file");
            oFile.close();

            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                System.out.println(key+"="+prop.getProperty(key));
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}