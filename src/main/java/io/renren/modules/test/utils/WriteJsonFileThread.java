package io.renren.modules.test.utils;

/**
 * @author CT
 * @date 2019/6/2914:05
 * @describe WriteFileThread用于
 */

public class WriteJsonFileThread extends Thread{
    String toWriteString = "";//定义线程内变量
    String filePath = "";

    public WriteJsonFileThread(String stringJson, String filePath){
        //定义带参数的构造函数,达到初始化线程内变量的值
        this.toWriteString = stringJson;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        WriteJsonFile.WriteConfigJson(this.toWriteString, this.filePath);
    }
}